<?php
ini_set('error_reporting', E_ALL);
ini_set('display_errors', 1);

$dbU='fer87';
$db = mysql_connect("fer87.db.8639285.hostedresource.com","fer87","b49a00efbd40") OR DIE ('Tareas de mantenimiento. Pruebe más tarde, por favor.');
mysql_select_db($dbU);

if (isset($_REQUEST["funcion"])) $funcion=$_REQUEST["funcion"];
if (isset($_REQUEST["idlog"])) $idlog=$_REQUEST["idlog"];
if (isset($_REQUEST["user"])) $userAut=$_REQUEST["user"];
if (isset($_REQUEST["pass"])) $passAut=$_REQUEST["pass"];
if (isset($_REQUEST["idcompra"])) $idcompra=$_REQUEST["idcompra"];
if (isset($_REQUEST["dat"])) $dat=$_REQUEST["dat"];

switch ($funcion) {
	//Para autenticarnos
	case "autenticacion":
		$datos=autenticacion($userAut,$passAut);
		break;
	//Para sacar los datos del usuario logueado
	case "datosUsuarioLogueado":
		$datos=datosUsuarioLogueado($idlog);
		break;
	case "getRegistroCompras":
		$datos=getRegistroCompras($idlog);
		break;
	case "getCompraDetallada":
		$datos=getCompraDetallada($idcompra);
		break;
	case "getCatalogo":
		$datos=getCatalogo();
		break;
	case "getWishlist":
		$datos=getWishlist($idlog);
		break;
	case "getWishlistID":
		$datos=getWishlistID($idlog);
		break;
	case "insertWishlist":
                $datos=insertWishlist($dat);
                break;
	case "deleteWishlist":
                $datos=deleteWishlist($dat);
                break;
	/*case "":
		
		break;*/
	default:
		$datos="La funcion no esta definida en los parametros";
		break;
}
print(json_encode($datos));

function autenticacion($username,$password) {
//Devuelve el id del usuario logueado si la identificación es correcta
	$sql = "SELECT id FROM usuario WHERE usuario='$username' AND password='$password'";
	$res = mysql_query($sql);
	$res= mysql_fetch_array($res);
	if ($res==false) $res=array("id"=>"undefined");
return $res;
}

function datosUsuarioLogueado($idu) {
//Sacamos datos del usuario logueado
	$sql="SELECT * FROM usuario WHERE id=$idu";
	$res = mysql_query($sql);
	$res= mysql_fetch_array($res);
	if ($res==false) $res=array("id"=>"undefined");
return $res;
}

function getRegistroCompras($idus) {
//Sacamos el registro de las compras
	$estados=sacarEstados();
	$final=array();
	if (esAdministrador($idus)==1)	$sql = "SELECT id, fecha, precio, tarjeta, ttarjeta, idu, usuario, status FROM compras ORDER BY fecha";
	else	$sql = "SELECT id, fecha, precio, tarjeta, ttarjeta, idu, usuario, status FROM compras WHERE idu=$idus ORDER BY fecha";
	$res = mysql_query($sql);
	if ($res==false) $final=array("id"=>"undefined");
	else {
		//foreach ($res as $row) {
		while ($row= mysql_fetch_array($res)) {
			$row['status']=$estados[$row['status']];
			$final[]=$row;
		}
	}
	return $final;
}

function getCompraDetallada($idreg) {
//Sacamos todos los productos de un registro de compra concreto
	//Sacamos el status de la compra en cuestion
	$estados=sacarEstados();
	$sql="SELECT status FROM compras WHERE id='$idreg'";
	$rs=mysql_query($sql);
	$rw= mysql_fetch_array($rs);
	$status=$estados[$rw['status']];
	$sql = "SELECT fecha,producto,cantidad,precio,usuario,tarjeta,ttarjeta FROM registro_compras WHERE idcompra='$idreg'";
	$res = mysql_query($sql);
	$compra=array();
	while ($row=mysql_fetch_array($res)) {
		$compra[]=array('idcompra' => $idreg, 'fecha'=> $row[0], 'producto'=> $row[1], 'cantidad'=> $row[2], 'precio'=> $row[3], 'usuario'=> $row[4], 'tarj'=> $row[5], 'ttarj'=> $row[6], 'status'=> $status);
	}
return $compra;
}

function getCatalogo() {
//Sacamos todos los productos
	$catalogo=array();
	$sql = "SELECT id, nombre, precio, categoria, descripcion, stock, ruta FROM productos ORDER BY id";
	$res = mysql_query($sql);
	while ($row=mysql_fetch_array($res)) {
		$row['ruta']=substr($row['ruta'],7);
		$imagen= "http://marketplace.sanchezsesena.com/Android/android_images/".$row['ruta'];
		$row['ruta']=$imagen;
		$catalogo[]=$row;
	}
return $catalogo;
}

function getWishlist($idlog) {
//Sacamos la wishlist
	$sql = "SELECT id_producto,nombre_producto FROM wishlist WHERE id_usuario=$idlog ORDER BY id_producto";
	$res = mysql_query($sql);
	$wishlist=array();
	while ($row=mysql_fetch_array($res)) {
		$sql2="SELECT precio,descripcion FROM productos WHERE id=".$row[0]." ORDER BY id";
                $rs=mysql_query($sql2);
                $rw= mysql_fetch_array($rs);
		$precio=$rw['precio'];
                $descripcion=$rw['descripcion'];
                $wishlist[]=array('idusuario' => $idlog, 'nombre_producto'=> $row[1], 'idproducto'=> $row[0], 'precio' => $precio, 'descripcion' => $descripcion);
	}
return $wishlist;
}

function getWishlistID($idlog) {
//Sacamos los ID de la wishlist
	$sql = "SELECT id FROM productos ORDER BY id";
	$sql2 = "SELECT id_producto FROM wishlist WHERE id_usuario=$idlog ORDER BY id_producto";
	$res = mysql_query($sql2);
	$wishlist=$comp=array();
	while ($row=mysql_fetch_array($res)) {
		$comp[$row[0]]=1;
	}
	$res = mysql_query($sql);
	while ($row=mysql_fetch_array($res)) {
                if (!isset($comp[$row[0]]))	$wishlist[]=array("esta" => "0");
		else	$wishlist[]=array("esta" => "1");
	}
return $wishlist;
}

function insertWishlist($dat) {
//Insertamos producto en Wishlist
	$data=explode(' - ',$dat);
	$sql = "INSERT INTO wishlist SELECT id,nombre,".$data[0]." FROM productos WHERE id=".$data[1]." LIMIT 1";
	//print ($sql);
	if (mysql_query($sql))  $ok="true";
        else $ok="false";
	$ok=array("esok"=>$ok);
return $ok;
}

function deleteWishlist($dat) {
//Insertamos producto en Wishlist
	$data=explode(' - ',$dat);
	$sql = "DELETE FROM wishlist WHERE id_producto=".$data[1]." AND id_usuario=".$data[0]." LIMIT 1";
	//print ($sql);
	if (mysql_query($sql))  $ok="true";
        else $ok="false";
	$ok=array("esok"=>$ok);
return $ok;
}

//SUBFUNCIONES

function esAdministrador($id) {
//Devuelve 1 si es administrador y 0 si no
	$sql="SELECT administrador FROM usuario WHERE id=$id";
	$rs=mysql_query($sql);
	$row= mysql_fetch_array($rs);
	$res=$row['administrador'];
return ($res);
}

function sacarEstados() {
//Sacamos los estados en string
	$status=array();
	$sql="SELECT id,descripcion FROM estados_compras";
	$rs=mysql_query($sql);
	while ($rw= mysql_fetch_array($rs)) {
		$status[$rw['id']]=$rw['descripcion'];
	}
return $status;
}
//SUBFUNCIONES

/////////////////////////////////////////////////////////////////////////////////
function datosGlobalesUsuario($iduser) {
//Sacamos todos los datos existentes sobre un usuario
	$sql = "SELECT id, usuario, password, nombre, apellido1, apellido2, fechaNacimiento, email, administrador, direccion FROM usuario where id=$iduser";
	$res = mysql_query($sql);
	while ($row= mysql_fetch_array($res)) {
		$id = $row[0];
		$users = $row[1];
		$pass = $row[2];
		$nombre = $row[3]." ".$row[4]." ".$row[5];
		$fNac = $row[6];
		$email = $row[7];
		$admin = $row[8];
		$dir = $row[9];
	}
return array($id,$users,$pass,$nombre,$fNac,$email,$admin,$dir);
}

function datosTodosUsuarios() {
//Sacamos los datos de todos los usuarios existentes
	$sql = "SELECT id, usuario, password, nombre, apellido1, apellido2, fechaNacimiento, email, administrador,direccion FROM usuario";
	$res = mysql_query($sql);
	while ($row= mysql_fetch_array($res)) {
		$id [$row[0]] = $row[0];
		$users [$row[0]] = $row[1];
		$pass [$row[0]] = $row[2];
		$nombre [$row[0]] = $row[3]." ".$row[4]." ".$row[5];
		$fNac [$row[0]] = $row[6];
		$email [$row[0]] = $row[7];
		$admin [$row[0]] = $row[8];
		$dir [$row[0]] = $row[9];
	}
return array($id,$users,$pass,$nombre,$fNac,$email,$admin,$dir);
}

function updateCambioDatos($name,$ap1,$ap2,$fNac,$username,$pass,$email,$idu) {
//UPDATE a los datos de usuario
	$sql = "UPDATE usuario SET nombre=$name, apellido1=$ap1, apellido2=$ap2, fechaNacimiento=$fNac, usuario=$username, password=$pass, email=$email WHERE id=$idu LIMIT 1";
	if (mysql_query($sql))	$ok=true;
	else $ok=false;
return $ok;
}

function checkDatosNewUser($username) {
//Comprobamos que el nombre de usuario no esté ya cogido
	$sql= "SELECT id FROM usuario WHERE usuario='$username' LIMIT 1";
	$res = mysql_query($sql);
	$row= mysql_fetch_array($res);
	$id=$row['id'];
	if ((empty($id))||(!isset($id))) $ok=true;
	else $ok=false;
return array($ok,$id);
}

function insertNewUser($name,$ap1,$ap2,$dir,$fNac,$username,$pass,$email) {
//INSERT usuario nuevo
	$sql = "INSERT INTO usuario (nombre, apellido1, apellido2, direccion, fechaNacimiento, usuario, password, email, administrador) VALUES ('$name', '$ap1', '$ap2', '$dir', '$fNac', '$username', '$pass', '$email', 0)";
	if (mysql_query($sql))	$ok=true;
	else $ok=false;
return $ok;
}

function getCategorias() {
//Sacamos todas las categorías con sus nombres
	$sql = "SELECT id, nombre FROM categoria";
	$res = mysql_query($sql);
	while ($row=mysql_fetch_array($res)) {
		$idc [$row[0]] = $row[0];
		$nombrec [$row[0]] = $row[1];
	}
return array($idc,$nombrec);
}

function getProducto($idprod){
//Sacamos los datos de un producto en base a su id
	$sql = "SELECT id, nombre, precio, categoria, descripcion, Stock, Ruta FROM productos WHERE id=$idprod";
	$res = mysql_query($sql);
	$row= mysql_fetch_array($res);
	$id = $row[0];
	$nombre = $row[1];
	$precio = $row[2];
	$cat = $row[3];
	$descrip = $row[4];
	$stock = $row[5];
	$imagen= $row[6];
return array($id,$nombre,$precio,$cat,$descrip,$stock,$imagen);
}

function comprobarStock($cesta) {
//Miramos stock de los productos
	foreach ($cesta as $ind) {
		$sql = "SELECT id, Stock FROM productos WHERE id=".$ind['id'];
		$res = mysql_query($sql);
		while ($row= mysql_fetch_array($res)) {
			$stock [$row[0]] = $row[1];
		}
	}	
return $stock;
}

function insertarCompras ($id,$date,$precioT,$user,$tar,$tipo,$usL,$status) {
//Insertamos en la tabla compras
	$sql = "INSERT INTO compras (id,fecha, precio, usuario, tarjeta, ttarjeta, idu, status) VALUES ('$id','$date',$precioT,'$user',$tar,'$tipo','$usL',$status)";
	if (mysql_query($sql))$ok=true;
	else $ok=false;
return $ok;
}

function insertarComprasFuturas ($id,$date,$precioT,$user,$tar,$tipo,$usL,$status=0) {
//Insertamos en la tabla compras las compras futuras
	$sql = "INSERT INTO compras (id,fecha, precio, usuario, tarjeta, ttarjeta, idu, status) VALUES ('$id','$date',$precioT,'$user',$tar,'$tipo','$usL',$status)";
	if (mysql_query($sql))$ok=true;
	else $ok=false;
return $ok;
}

function insertarCompraDetallada ($idcompra,$fecha,$producto,$cantidad,$precio,$usuario,$tarjeta,$ttarjeta) {
//Insertamos todos los productos de una compra
	$sql="INSERT INTO registro_compras VALUES ('$idcompra','$fecha','$producto',$cantidad,$precio,'$usuario',$tarjeta,'$ttarjeta')";
	if (mysql_query($sql))$ok=true;
	else $ok=false;
return $ok;
}

function updatearStockProd ($nuevoStock,$idprod) {
//Actualizamos el stock de los productos
	$sql = "UPDATE productos SET Stock=$nuevoStock WHERE id=$idprod LIMIT 1";
	if (mysql_query($sql)) $ok=true;
	else $ok=false;
return $ok;
}

function addDays($fecha, $dias){
//Funcion para sumar o restar dias a una fecha en formato YYYY-MM-DD
    list($y,$m,$d) = explode('-',$fecha);
	$y=intval($y); $m=intval($m); $d=intval($d);
	$nueva = strtotime("+$dias day", mktime(0,0,0,$m,$d,$y));
	$fecha_new = date('Y-m-d', $nueva);
    return $fecha_new;
}

?>
