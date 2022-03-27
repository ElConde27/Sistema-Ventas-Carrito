
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Locale"%>
<%@page import="com.alexander.config.Fecha"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
        <link href="css/estilos1.css" rel="stylesheet" type="text/css"/>
        <title>Carrito de Compras</title>
    </head>
    <body>       
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarTogglerDemo03" aria-controls="navbarTogglerDemo03" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <a class="navbar-brand" href="#"><i>Alexander's Store</i></a>
            <div class="collapse navbar-collapse" id="navbarTogglerDemo03">
                <ul class="navbar-nav mr-auto mt-2 mt-lg-0">
                    <li class="nav-item active">
                        <a class="nav-link" href="./Controlador?accion=Nuevo"><i class="fas fa-home"></i> Home<span class="sr-only">(current)</span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="./Controlador?accion=home"><i class="fas fa-plus-circle"></i> Ofertas del Dia</a>
                    </li>                   
                    <li class="nav-item">
                        <a class="nav-link" href="./Controlador?accion=home"><i class="fas fa-plus-circle"></i> Seguir Comprando</a>
                    </li>                  
                </ul>                                            
                <ul class="navbar-nav btn-group my-2 my-lg-0" role="group">
                    <a style="color: white; cursor: pointer" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fas fa-user-tie"></i> ${logueo}</a>
                    <div class="dropdown-menu text-center dropdown-menu-right">
                        <a class="dropdown-item" href="#"><img src="img/user.png" alt="60" height="60"/></a>                        
                        <a class="dropdown-item" href="#">${user}</a>
                        <a class="dropdown-item" href="#" data-toggle="modal" data-target="#myModal">${correo}</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="Controlador?accion=MisCompras">Mis Compras</a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="Controlador?accion=Salir"> <i class="fas fa-arrow-right"> Salir</i></a>                        
                    </div>
                </ul>     
            </div>
        </nav>
        <div class="container mt-4">
            <div class="d-flex">
                <h2>Carrito</h2>     
                <%
                    Fecha fechaSistema = new Fecha();
                %>                     
                <p class="ml-auto"><%= fechaSistema.Fecha()%></p>                               
            </div>                    
            <div class="row">             
                <div class="col-lg-9">                 
                    <table class="table">
                        <thead class="thead-light">
                            <tr class="text-center">
                                <th>Item</th>                               
                                <th>Articulo</th>
                                <th>Descripcion</th>
                                <th>Precio</th>
                                <th>Cantidad</th>                       
                                <th>Total</th>                       
                                <th>Acciones</th>                       
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="c" items="${Carrito}"> 
                                <tr class="text-center tr">
                                    <td>${c.getItem()}</td>                                   
                                    <td>${c.getNombres()}
                                        <img src="${c.getImagen()}" width="130" height="110">
                                    </td>
                                    <td>${c.getDescripcion()}</td>
                                    <td>${c.getPrecioCompra()}</td>
                                    <td>        
                                        <input type="hidden" id="item1" value="${c.getIdProducto()}">
                                        <input type="number" min="1" max="10"  id="Cant" class=" form-control text-center" value="${c.getCantidad()}">
                                    </td>   
                                    <td>${c.getSubTotal()}</td>                           
                                    <td class="text-center">                                         
                                        <input type="hidden" id="item2" value="${c.getIdProducto()}">
                                        <a id="deleteItem" href="#" class="btn"><i class="fas fa-trash-alt"></i></a>                                            
                                    </td>                           
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>                    
                </div>
                <div class="col-lg-3">                  
                    <div class="card">
                        <div class="card-header">
                            Generar Compra
                        </div>
                        <div class="card-body">
                            <label>Subtotal:</label>
                            <a class="form-control text-center"><i class="fas fa-dollar-sign h5"> ${totalPagar}0</i></a>
                            <label>Precio Envio:</label>
                            <a class="form-control text-center"><i class="fas fa-dollar-sign h5"> 0.00</i></a>
                            <label>Descuento:</label>
                            <a class="form-control text-center"><i class="fas fa-dollar-sign h5"> 0.00</i></a>
                            <label>Total a Pagar:</label>
                            <a class="form-control text-center"><i class="fas fa-dollar-sign h4 primary"> ${totalPagar}0</i></a>
                        </div>
                        <div class="card-footer">
                            <a href="Controlador?accion=GenerarCompra" class="btn btn-danger btn-block">Generar Compra</a>
                            <a href="#" data-toggle="modal" data-target="#myModalPago" class="btn btn-info btn-block">Realizar Pago</a>
                        </div>
                    </div>
                </div>
            </div>          
        </div>
        <!-- Modal Iniciar Session o Registrarse -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="container col-lg-9">                   
                    <div class="modal-content">                   
                        <div class="pr-2 pt-1">                         
                            <button type="button" class="close" data-dismiss="modal">
                                <span aria-hidden="true">&times;</span>
                            </button>                    
                        </div>
                        <div class="text-center">                         
                            <img src="img/user.png" width="100" height="100">                         
                        </div>
                        <div class="modal-header text-center">                      
                            <ul class="nav nav-pills">                           
                                <li class="nav-item">
                                    <a class="nav-link active" data-toggle="pill" href="#pills-iniciar">Iniciar Sesion</a>
                                </li>
                                <li class="nav-item">
                                    <a class="nav-link" data-toggle="pill" href="#pills-registrar">Registrarse</a>
                                </li>                          
                            </ul>  
                        </div>
                        <div class="modal-body"> 
                            <div class="tab-content" id="pills-tabContent">
                                <!-- Iniciar Session -->
                                <div class="tab-pane fade show active" id="pills-iniciar" role="tabpanel">
                                    <form action="Controlador">
                                        <div class="form-group">
                                            <label>Email address</label>
                                            <input type="email" name="txtemail" class="form-control" placeholder="email@example.com">
                                        </div>
                                        <div class="form-group">
                                            <label>Password</label>
                                            <input type="password" name="txtpass" class="form-control" placeholder="Password">
                                        </div>                                   
                                        <button type="submit" name="accion" value="Validar" class="btn btn-danger btn-block">Iniciar</button>
                                    </form>
                                </div>
                                <!-- Registrarse -->
                                <div class="tab-pane fade" id="pills-registrar" role="tabpanel">
                                    <form action="Controlador">                               
                                        <div class="form-group">
                                            <label>Nombres</label>
                                            <input type="text" name="txtnom" class="form-control" placeholder="Leo Perez">
                                        </div>
                                        <div class="form-group">
                                            <label>Dni</label>
                                            <input type="number" name="txtdni" class="form-control" placeholder="01245678">
                                        </div>
                                        <div class="form-group">
                                            <label>Direccion</label>
                                            <input type="text" name="txtdire" class="form-control" placeholder="Chiclayo - La Victoria">
                                        </div>
                                        <div class="form-group">
                                            <label>Email address</label>
                                            <input type="email" name="txtemail" class="form-control" placeholder="email@example.com">
                                        </div>
                                        <div class="form-group">
                                            <label>Password</label>
                                            <input type="password" name="txtpass" class="form-control" placeholder="Password">
                                        </div>                                  
                                        <button type="submit" name="accion" value="Registrar" class="btn btn-danger btn-block">Crear Cuenta</button>
                                    </form>
                                </div>                          
                            </div> 
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal de Pago -->
        <div class="modal fade" id="myModalPago" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-dialog-centered" role="document">         
                <div class="modal-content">                   
                    <div class="modal-header text-center"> 
                        <label><i class="fa-dollar-sign"></i>Realizar Pago</label>
                        <button type="button" class="close" data-dismiss="modal">
                            <span aria-hidden="true">&times;</span>
                        </button>  
                    </div>
                    <div class="modal-body"> 
                        <div class="tab-content" id="pills-tabContent">
                            <!-- Metodo Pago -->
                            <div class="tab-pane fade show active" id="pills-iniciar" role="tabpanel">
                                <form action="Controlador">
                                    <div class="form-group">
                                        <label>Card address</label>
                                        <input type="text" class="form-control" placeholder="9999-9999-9999-9999">
                                    </div>
                                    <div class="form-group">
                                        <label>Codigo Seguridad</label>
                                        <input type="text" class="form-control" placeholder="xxxx">
                                    </div>
                                    <div class="form-group">
                                        <label>Monto</label>
                                        <input type="text" name="txtmonto" value="$.${totalPagar}0" class="form-control h1">
                                    </div>                                   
                                    <button type="submit" name="accion" value="RealizarPago" class="btn btn-info btn-block">Pagar</button>
                                </form>
                            </div>

                        </div> 
                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.4.1.js" integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <script src="js/funciones.js" type="text/javascript"></script>
    </body>
</html>
