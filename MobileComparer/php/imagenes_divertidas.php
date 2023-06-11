<?php

/* Se requiere usuario y contraseña para comprobar la procedencia de la peticion */
if ((isset($_GET['user'])) && ($_GET['user']=="re") && (isset($_GET['pass'])) && ($_GET['pass']=="re")) {

	/* determinamos que funcion debemos ejecutar */
	$tipo_funcion = isset($_GET['tipo_funcion']) ? $_GET['tipo_funcion'] : "nada";
	
	/* determinamos el idioma a utilizar */
	$lang = isset($_GET['lang']) ? $_GET['lang'] : "es";
	if ($lang!="en" && $lang!="es")	$lang = "es";
	
	/* obtenemos otros parametros auxiliares */
	$tipo_categoria = isset($_GET['categoria']) ? utf8_decode($_GET['categoria']) : "";
	
	/*Dependiendo del parametro tipo_funcion llamamos a una funcion u a otra*/
	if ($tipo_funcion=="categorias")	$result=categorias();
	else if ($tipo_funcion=="listado_por_categoria")	$result=listado_por_categoria($tipo_categoria);
	else	$result=array();

	/* sacamos la informacion en json */
	header('Content-type: application/json');
	echo json_encode($result);
}
	
/* FUNCIONES USADAS */

//Funcion para sacar los nombres de todos las categorías en la tabla	
function categorias() {

    /* Conectamos a la BD */
    $link=conectarBD();

	/* hacemos la consulta para sacar todas las categorias existentes en la DB */
	$query = "SELECT DISTINCT (categoria) FROM imagenes";
	$res = mysqli_query($link,$query) or die('Query error');

	/* Creamos un array global de los resultados */
	$resultado = array();
	$resultado[] = "Todas";
	$resultado[] = "Selección aleatoria";
	
	/* Creamos una declaración preparada para evitar injections */
    if($stmt = $link -> prepare($query)) {

      /* Ejecutamos la declaración */
      $stmt -> execute();

      /* Enlazamos los resultados */
      $stmt -> bind_result($cat_col);

      /* Extraemos los valores y los vamos asignando a un array global de resultados */
	  while ($stmt->fetch()) {
			$resultado[]= utf8_encode($cat_col);
      }

      /* Cerramos declaración */
      $stmt -> close();
    }

    /* Desconectamos de la DB */
	@mysqli_close($link);

	return (array("categorias" => $resultado));
}

//Funcion para sacar las urls y títulos de todas las imágenes de la DB
function listado_por_categoria($cat) {

    /* Conectamos a la BD */
    $link=conectarBD();
	
	/* Hacemos la consulta para sacar todas las imagenes existentes en la DB para la categoria pedida */
	$cat = trim($cat);
	if ($cat == "" || $cat == "Todas" || $cat == utf8_decode("Selección aleatoria"))
		$query = "SELECT titulo,url FROM imagenes ORDER BY id DESC";
	else
		$query = "SELECT titulo,url FROM imagenes WHERE categoria = ? ORDER BY id DESC";
	
	$resultado = array();
	
	/* Creamos una declaración preparada para evitar injections */
    if($stmt = $link -> prepare($query)) {

      /* Agregamos los parametros (el primero indica el tipo de datos que habrá) */
	  if (!($cat == "" || $cat == "Todas" || $cat == utf8_decode("Selección aleatoria")))
		$stmt -> bind_param("s",$cat);

      /* Ejecutamos la declaración */
      $stmt -> execute();

      /* Enlazamos los resultados */
      $stmt -> bind_result($title_col, $url_col);

      /* Extraemos los valores y los vamos asignando a un array global de resultados */
	  while ($stmt->fetch()) {
			$resultado[]= utf8_encode($title_col)."&".$url_col;
      }
	  if ($cat == utf8_decode("Selección aleatoria"))
		shuffle($resultado);

      /* Cerramos declaración */
      $stmt -> close();
    }

    /* Desconectamos de la DB */
	@mysqli_close($link);

	return (array("urls" => $resultado));
}

/* FUNCIONES AUXILIARES */

function conectarBD() {
    /* Conectamos a la base de datos */	
	$link = new mysqli("noway","noway","noway",'noway') OR DIE ('Tareas de mantenimiento. Pruebe más tarde, por favor.');
	if ($mysqli->connect_errno) {
		printf("Connect failed: %s\n", $mysqli->connect_error);
		exit();
	}
    return $link;
}

?>