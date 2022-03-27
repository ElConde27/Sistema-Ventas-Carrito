package com.alexander.controlador;

import com.alexander.config.Fecha;
import com.alexander.modelo.Carrito;
import com.alexander.modelo.Cliente;
import com.alexander.modelo.ClienteDAO;
import com.alexander.modelo.Compra;
import com.alexander.modelo.ComprasDAO;
import com.alexander.modelo.DetalleCompra;
import com.alexander.modelo.Pago;
import com.alexander.modelo.Producto;
import com.alexander.modelo.ProductoDAO;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class Controlador extends HttpServlet {

    Pago pago = new Pago();
    Cliente cl = new Cliente();
    ClienteDAO cldao = new ClienteDAO();
    ComprasDAO cdao = new ComprasDAO();
    ProductoDAO pdao = new ProductoDAO();
    Producto p = new Producto();
    int item = 0;
    int cantidad = 1;
    double subtotal = 0.0;
    double totalPagar = 0.0;

    List<Carrito> listaProductos = new ArrayList<>();
    List productos = new ArrayList();

    String logueo = "Iniciar Sesion";
    String correo = "Iniciar Sesion";

    int idcompra;
    int idpago;
    double montopagar;
    int idProducto = 0;

    Carrito car = new Carrito();

    Fecha fechaSistem = new Fecha();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute("logueo", logueo);
        session.setAttribute("correo", correo);
        productos = pdao.listar();
        String accion = request.getParameter("accion");

        switch (accion) {
            case "carrito":
                totalPagar = 0.0;
                item = 0;
                request.setAttribute("Carrito", listaProductos);
                for (int i = 0; i < listaProductos.size(); i++) {
                    totalPagar = totalPagar + listaProductos.get(i).getSubTotal();
                    listaProductos.get(i).setItem(item + i + 1);
                }
                request.setAttribute("totalPagar", totalPagar);
                request.getRequestDispatcher("vistas/carrito.jsp").forward(request, response);
                break;
            case "Comprar":
                agregarCarrito(request);
                request.getRequestDispatcher("Controlador?accion=carrito").forward(request, response);
                break;
            case "AgregarCarrito":
                agregarCarrito(request);
                request.setAttribute("cont", listaProductos.size());
                request.getRequestDispatcher("Controlador?accion=home").forward(request, response);
                break;
            case "deleteProducto":
                idProducto = Integer.parseInt(request.getParameter("id"));
                if (listaProductos != null) {
                    for (int j = 0; j < listaProductos.size(); j++) {
                        if (listaProductos.get(j).getIdProducto() == idProducto) {
                            listaProductos.remove(j);
                        }
                    }
                }
                break;
            case "updateCantidad":
                idProducto = Integer.parseInt(request.getParameter("id"));
                int cant = Integer.parseInt(request.getParameter("cantidad"));
                for (int j = 0; j < listaProductos.size(); j++) {
                    if (listaProductos.get(j).getIdProducto() == idProducto) {
                        listaProductos.get(j).setCantidad(cant);
                        listaProductos.get(j).setSubTotal(listaProductos.get(j).getPrecioCompra() * cant);
                    }
                }
                break;

            case "Validar":
                String email = request.getParameter("txtemail");
                String pass = request.getParameter("txtpass");
                cl = cldao.Validar(email, pass);
                if (cl.getId() != 0) {
                    logueo = cl.getNombres();
                    correo = cl.getEmail();
                }
                request.getRequestDispatcher("Controlador?accion=carrito").forward(request, response);
                break;
            case "Registrar":
                String nom = request.getParameter("txtnom");
                String dni = request.getParameter("txtdni");
                String em = request.getParameter("txtemail");
                String pas = request.getParameter("txtpass");
                String dir = request.getParameter("txtdire");
                cl.setNombres(nom);
                cl.setDni(dni);
                cl.setEmail(em);
                cl.setPass(pas);
                cl.setDireccion(dir);
                cldao.AgregarCliente(cl);
                request.getRequestDispatcher("Controlador?accion=carrito").forward(request, response);
                break;
            case "Nuevo":
                listaProductos = new ArrayList();
                request.getRequestDispatcher("Controlador?accion=home").forward(request, response);
                break;
            case "Buscar":
                String nombre = request.getParameter("txtbuscar");
                productos = pdao.buscar(nombre);
                request.setAttribute("cont", listaProductos.size());
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            case "RealizarPago":
                montopagar = totalPagar;
                if (cl.getId() != 0 && montopagar > 0) {
                    cdao.Pagar(montopagar);
                    request.getRequestDispatcher("Controlador?accion=carrito").forward(request, response);
                } else {
                    montopagar = 0;
                    request.getRequestDispatcher("Controlador?accion=carrito").forward(request, response);
                }
                break;
            case "GenerarCompra":
                idpago = cdao.IdPago();
                if (cl.getId() != 0 && listaProductos.size() != 0 && montopagar > 0) {
                    if (totalPagar > 0.0) {
                        Compra co = new Compra();
                        co.setIdCliente(cl.getId());
                        co.setFecha(fechaSistem.FechaBD());
                        co.setMonto(totalPagar);
                        co.setIdPago(idpago);
                        co.setEstado("Cancelado - En Proceso de Envio");
                        cdao.guardarCompra(co);
                        montopagar = 0;

                        idcompra = cdao.IdCompra();
                        for (int i = 0; i < listaProductos.size(); i++) {
                            DetalleCompra dc = new DetalleCompra();
                            dc.setIdcompra(idcompra);
                            dc.setIdproducto(listaProductos.get(i).getIdProducto());
                            dc.setCantidad(listaProductos.get(i).getCantidad());
                            dc.setPrecioCompra(listaProductos.get(i).getPrecioCompra());
                            cdao.guardarDetalleCompra(dc);
                        }
                        listaProductos = new ArrayList<>();
                        List compra = cdao.misCompras(cl.getId());
                        request.setAttribute("myCompras", compra);
                        request.getRequestDispatcher("vistas/compras.jsp").forward(request, response);
                    } else {
                        request.getRequestDispatcher("Controlador?accion=home").forward(request, response);
                    }
                } else {
                    request.getRequestDispatcher("Controlador?accion=carrito").forward(request, response);
                }
                break;
            case "MisCompras":
                if (cl.getId() != 0) {
                    List compra = cdao.misCompras(cl.getId());
                    request.setAttribute("myCompras", compra);
                    request.getRequestDispatcher("vistas/compras.jsp").forward(request, response);
                } else if (listaProductos.size() > 0) {
                    request.getRequestDispatcher("Controlador?accion=carrito").forward(request, response);
                } else {
                    request.getRequestDispatcher("Controlador?accion=home").forward(request, response);
                }
                break;
            case "verDetalle":
                totalPagar = 0.0;
                int idcompras = Integer.parseInt(request.getParameter("idcompra"));
                List<DetalleCompra> detalle = cdao.Detalle(idcompras);
                request.setAttribute("myDetalle", detalle);
                for (int i = 0; i < detalle.size(); i++) {
                    totalPagar = totalPagar + (detalle.get(i).getPrecioCompra() * detalle.get(i).getCantidad());
                }
                request.setAttribute("montoPagar", totalPagar);
                request.getRequestDispatcher("vistas/DetalleCompra.jsp").forward(request, response);
                break;
            case "NuevoProducto":
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("vistas/addProducto.jsp").forward(request, response);
                break;
            case "GuardarProducto":
                ArrayList<String> pro=new ArrayList<>();
                try {
                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload fileUpload = new ServletFileUpload(factory);
                    List items = fileUpload.parseRequest(request);
                    for (int i = 0; i < items.size(); i++) {
                        FileItem fileItem = (FileItem) items.get(i);
                        if(!fileItem.isFormField()){
                            File file=new File("C:\\Imagenes\\sistemaCarrito\\"+fileItem.getName());
                            fileItem.write(file);
                            p.setImagen(file.getAbsolutePath());
                        }else{
                            pro.add(fileItem.getString());                            
                        }
                    }
                    p.setNombres(pro.get(0));
                    p.setDescripcion(pro.get(1));
                    p.setPrecio(Double.parseDouble(pro.get(2)));
                    p.setStock(Integer.parseInt(pro.get(3)));
                    pdao.AgregarNuevoProducto(p);

                } catch (Exception e) {
                    System.err.println(""+e);
                }
                request.getRequestDispatcher("Controlador?accion=NuevoProducto").forward(request, response);
                break;

            case "Salir":
                listaProductos = new ArrayList();
                cl = new Cliente();
                session.invalidate();
                logueo = "Iniciar Sesion";
                correo = "Iniciar Sesion";
                request.getRequestDispatcher("Controlador?accion=Salirr").forward(request, response);
                break;
            default:
                request.setAttribute("cont", listaProductos.size());
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public void agregarCarrito(HttpServletRequest request) {
        cantidad = 1;
        int pos = 0;
        int idpp = Integer.parseInt(request.getParameter("id"));
        if (listaProductos.size() > 0) {
            for (int i = 0; i < listaProductos.size(); i++) {
                if (listaProductos.get(i).getIdProducto() == idpp) {
                    pos = i;
                }
            }
            if (idpp == listaProductos.get(pos).getIdProducto()) {
                cantidad = listaProductos.get(pos).getCantidad() + cantidad;
                subtotal = listaProductos.get(pos).getPrecioCompra() * cantidad;
                listaProductos.get(pos).setCantidad(cantidad);
                listaProductos.get(pos).setSubTotal(subtotal);
            } else {
                car = new Carrito();
                p = pdao.listarId(idpp);
                car.setIdProducto(p.getId());
                car.setNombres(p.getNombres());
                car.setImagen(p.getImagen());
                car.setDescripcion(p.getDescripcion());
                car.setPrecioCompra(p.getPrecio());
                car.setCantidad(cantidad);
                subtotal = cantidad * p.getPrecio();
                car.setSubTotal(subtotal);
                listaProductos.add(car);
            }
        } else {
            car = new Carrito();
            p = pdao.listarId(idpp);
            car.setIdProducto(p.getId());
            car.setNombres(p.getNombres());
            car.setImagen(p.getImagen());
            car.setDescripcion(p.getDescripcion());
            car.setPrecioCompra(p.getPrecio());
            car.setCantidad(cantidad);
            subtotal = cantidad * p.getPrecio();
            car.setSubTotal(subtotal);
            listaProductos.add(car);
        }
    }
}
