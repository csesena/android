<?php

/* Se requiere usuario y contraseña para comprobar la procedencia de la peticion */
if (isset($_GET['user']) && $_GET['user']=="lolation") {

	/* Conversion Dollar/Euro y Peso/Euro */
	$conversion_dollar = 0.750;
	$conversion_peso = 0.0554;
	$conversion_rupia = 0.01197;
	$conversion_pound = 1.219;

	/* determinamos que funcion debemos ejecutar */
	$tipo_funcion = isset($_GET['tipo_funcion']) ? $_GET['tipo_funcion'] : "nada";
	
	/* determinamos el idioma a utilizar */
	$lang = isset($_GET['lang']) ? $_GET['lang'] : "es";
	$moneda = isset($_GET['moneda']) ? $_GET['moneda'] : "euro";
	if ($lang!="en" && $lang!="es")	$lang = "en";
	if ($moneda!="euro" && $moneda!="dolar" && $moneda!="peso" && $moneda!="rupia" && $moneda!="libra")	$moneda = "euro";
	
	/*Dependiendo del parametro tipo_funcion llamamos a una funcion u a otra*/
	if ($tipo_funcion=="nombres_moviles")	$result=nombres_moviles();
	else if ($tipo_funcion=="nombres_moviles_iddesc")	$result=nombres_moviles_iddesc();
	else if ($tipo_funcion=="datos_un_movil")	$result=datos_un_movil($lang, $moneda);
	else if ($tipo_funcion=="datos_dos_moviles")	$result=datos_dos_moviles($lang, $moneda);
	else if ($tipo_funcion=="movil_rango_precios")	$result=movil_rango_precios($lang, $moneda);
	else if ($tipo_funcion=="movil_so")	$result=movil_so();
	else if ($tipo_funcion=="ranking")	$result=ranking();
    else if ($tipo_funcion=="calidad_precio")    $result=calidad_precio();
	else if ($tipo_funcion=="movil_id")	$result=movil_id();
    else if ($tipo_funcion=="filtro")    $result=filtro();
    else if ($tipo_funcion=="get_maestros")    $result=get_maestros($lang);
	else	$result=array();

	/* sacamos la informacion en json */
	header('Content-type: application/json');
	echo json_encode($result);
}
	
/* FUNCIONES USADAS */

//Funcion para sacar los nombres de todos los móviles en la tabla	
function nombres_moviles() {

    /* Conectamos a la BD */
    $link=conectarBD();

	/* hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT nombre FROM moviles ORDER BY esfavorito DESC , nombre";
	$res = mysql_query($query,$link) or die('Query error:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
           while ($row= mysql_fetch_array($res)) {
		$resultado[]= $row['nombre'];
           }
	}

    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
}

//Funcion para sacar los nombres de todos los móviles en la tabla ordenados por id inverso (para saber las ultimas adiciones)
function nombres_moviles_iddesc() {

    /* Conectamos a la BD */
    $link=conectarBD();

	/* hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT nombre FROM moviles ORDER BY id DESC LIMIT 50";
	$res = mysql_query($query,$link) or die('Query error:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
           while ($row= mysql_fetch_array($res)) {
		$resultado[]= $row['nombre'];
           }
	}

    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
}

//Funcion para sacar los datos del movil seleccionado
function datos_un_movil($lang = "en", $moneda = "euro") {
	global $conversion_dollar, $conversion_peso, $conversion_rupia, $conversion_pound;

    /* Conectamos a la BD */
    $link=conectarBD();

	/* Obtenemos los parametros adicionales para hacer la query*/
	$nombre=isset($_GET['nombre_movil']) ? $_GET['nombre_movil'] : "Samsung Galaxy SIII";

	/* hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT id, nombre, procesador, gpu, ram, memoria, card_slot, so, bateria, pantalla, res_pantalla, tamano_pantalla, proteccion, camara, conectividad, nfc, gps, radio, medidas, peso, precio, comentario, comentario_eng, imagen FROM moviles WHERE nombre = '$nombre' LIMIT 1";
        mysql_query('SET CHARACTER SET utf8');
	$res = mysql_query($query,$link) or die('Errant query:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
		$row = mysql_fetch_array($res);
		for ($i=0; $i<=23; $i++){
			unset($row[$i]);
		}
		
		/* Realizamos las consultas para saber la puntuacion */
		$total=0;
		
		/* Procesador */
		$query = "SELECT puntuacion FROM procesador WHERE nombre = '".$row['procesador']."' LIMIT 1";
		$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
		if(mysql_num_rows($respunt)) {
			$rowpunt = mysql_fetch_array($respunt);
			$total += $rowpunt["puntuacion"];
		}
		
		/* GPU */
		$query = "SELECT puntuacion FROM gpu WHERE nombre = '".$row['gpu']."' LIMIT 1";
		$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
		if(mysql_num_rows($respunt)) {
			$rowpunt = mysql_fetch_array($respunt);
			$total += $rowpunt["puntuacion"];
		}
		
		/* SO */
		$query = "SELECT puntuacion FROM so WHERE nombre = '".$row['so']."' LIMIT 1";
		$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
		if(mysql_num_rows($respunt)) {
			$rowpunt = mysql_fetch_array($respunt);
			$total += $rowpunt["puntuacion"];
		}
		
		/* Pantalla */
		$query = "SELECT puntuacion FROM pantalla WHERE nombre = '".$row['pantalla']."' LIMIT 1";
		$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
		if(mysql_num_rows($respunt)) {
			$rowpunt = mysql_fetch_array($respunt);
			$total += $rowpunt["puntuacion"];
		}
		
		/* Proteccion Pantalla */
		$query = "SELECT puntuacion FROM proteccion_pantalla WHERE nombre = '".$row['proteccion']."' LIMIT 1";
		$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
		if(mysql_num_rows($respunt)) {
			$rowpunt = mysql_fetch_array($respunt);
			$total += $rowpunt["puntuacion"];
		}
		
		/* Camara */
		$query = "SELECT puntuacion FROM camara WHERE nombre = '".$row['camara']."' LIMIT 1";
		$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
		if(mysql_num_rows($respunt)) {
			$rowpunt = mysql_fetch_array($respunt);
			$total += $rowpunt["puntuacion"];
		}
		
		/* Sacamos el resto de la puntuacion y formateamos los datos de salida a gusto */
		$total += ($row['ram']/1.5) + ($row['memoria']/100) + $row['card_slot'] + ($row['bateria']/1.5) + ($row['tamano_pantalla']*230) + $row['conectividad'];
		$res_tokens = explode (" ",$row['res_pantalla']);
		$punt_res = ($res_tokens[0] + $res_tokens[2])/3;
		$total += $punt_res + $row['nfc'] + $row['gps'] + $row['radio'];
		$row['gpu'] = str_replace("Integrada","Built-in",$row['gpu']);
		$row['ram'] = $row['ram'] . " MB";
		$row['memoria'] = ($row['memoria']/1000) . " GB";
		if ($row['card_slot']>0) {
			if ($lang == "es")	$row['card_slot'] = "Sí";
			else	$row['card_slot'] = "Yes";
		}
		else	$row['card_slot'] = "No";
		$row['bateria'] = $row['bateria'] . " mAh";
		if ($lang == "es")
			$row['tamano_pantalla'] = $row['tamano_pantalla'] . " pulgadas";
		else
			$row['tamano_pantalla'] = $row['tamano_pantalla'] . " inches";
		$row['res_pantalla'] = $row['res_pantalla'] . " pxs";
		if (!isset($row['proteccion']))	$row['proteccion'] = "No";
		if ($lang != "es")	$row['proteccion'] = str_replace(" y "," and ",$row['proteccion']);
		if (is_null($row['camara']))	$row['camara'] = "-";
		if ($lang != "es") {
			$spanish = array(" y ", " con ", "zoom óptico", "delantera", "trasera");
			$english   = array(" and ", " with ", "optical zoom", "front", "rear");
			$row['camara'] = str_replace($spanish, $english, $row['camara']);
		}
		if ($row['conectividad']=="330") {
			if ($lang == "es")	$row['conectividad']="Bluetooth y Wifi";
			else	$row['conectividad']="Bluetooth and Wifi";
		}	else if ($row['conectividad']=="333")	{
			if ($lang == "es")	$row['conectividad']="Bluetooth, Wifi e IR";
			else	$row['conectividad']="Bluetooth, Wifi and IR";
		}
		else $row['conectividad']="Wifi";
		if ($row['nfc']>250) {
			if ($lang == "es")	$row['nfc'] = "Sí";
			else	$row['nfc'] = "Yes";
		} else {
			if ($row['nfc']==250) {
				if ($lang == "es")	$row['nfc'] = "Opcional";
				else	$row['nfc'] = "Optional";
			}
			else	$row['nfc'] = "No";
		}
		if ($row['gps']>0) {
			if ($lang == "es")	$row['gps'] = "Sí";
			else	$row['gps'] = "Yes";
		}
		else	$row['gps'] = "No";
		if ($row['radio']>0) {
			if ($lang == "es")	$row['radio'] = "Sí";
			else	$row['radio'] = "Yes";
		}
		else	$row['radio'] = "No";
		$tokens = explode(" x ", $row['medidas']);
		$total -= ($tokens[0]+$tokens[1]+$tokens[2]);
		//$total -= $row['peso'] + $row['precio'];
		$total -= $row['peso'];
		if (is_null($row['medidas'])) {
			$row['medidas'] = "-";
		} else {
			if ($lang == "en")
				$row['medidas'] = number_format($tokens[0]/25.4,2)." in X ".number_format($tokens[1]/25.4,2)." in X ".number_format($tokens[2]/25.4,2)." in";
			else
				$row['medidas'] = $tokens[0]." mm X ".$tokens[1]." mm X ".$tokens[2]." mm";
		}
		if (is_null($row['peso'])) {
			$row['peso'] = "-";
		} else {
			if ($lang == "en")
				$row['peso'] = number_format($row['peso']/28.349,2) . " oz";
			else
				$row['peso'] = $row['peso'] . " gr.";
		}
		if ($row['precio']=="0" ) {
			if ($lang == "es")	$row['precio'] = "Descatalogado.";
			else	$row['precio'] = "No longer stocked.";
		} else if (!isset($row['precio'])) {
			if ($lang == "es")	$row['precio'] = "Pendiente de conocer.";
			else	$row['precio'] = "Not yet known.";
		} else {
			if ($moneda == "euro")	$row['precio'] = $row['precio'] . " euros";
			else if ($moneda == "dolar")	$row['precio'] = round($row['precio']/$conversion_dollar) . " dollars";
			else if ($moneda == "libra")	$row['precio'] = round($row['precio']/$conversion_pound) . " pounds";
			else if ($moneda == "rupia")	$row['precio'] = round($row['precio']/$conversion_rupia) . " rupees";
			else	$row['precio'] = round($row['precio']/$conversion_peso) . " pesos";
			if ($lang == "es")	$row['precio'] = $row['precio'] . " (aprox.)";
			else	$row['precio'] = $row['precio'] . " (approx.)";
		}
		if ($lang != "es")	$row['comentario'] = $row['comentario_eng'];
		if (is_null($row['comentario']))  $row['comentario'] = "N/A";
		else { if($row['comentario']=="")	$row['comentario'] = "N/A"; }
		if (is_null($row['imagen']))  $row['imagen'] = "no";
		else { if($row['imagen']=="")	$row['imagen'] = "no"; }
		
		$row['puntuacion_total']=number_format($total,2,'.','');
		
		//Devolvemos el resultado
		$resultado = $row;
	}

    /* desconectamos de la DB */
	@mysql_close($link);

	return $resultado;
}

//Funcion para sacar los datos comparativos de los dos moviles elegidos	
function datos_dos_moviles($lang = "en", $moneda = "euro") {
	global $conversion_dollar, $conversion_peso, $conversion_rupia, $conversion_pound;

    /* Conectamos a la BD */
    $link=conectarBD();

	/* Obtenemos los parametros adicionales para hacer la query*/
	$nombre1=isset($_GET['nombre_movil_1']) ? $_GET['nombre_movil_1'] : "Samsung Galaxy SIII";
	$nombre2=isset($_GET['nombre_movil_2']) ? $_GET['nombre_movil_2'] : "Apple iPhone 5";

	/* hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT id,nombre,procesador,gpu,ram,memoria,card_slot,so,bateria,pantalla,res_pantalla,tamano_pantalla,proteccion,camara,conectividad,nfc,gps,radio,medidas,peso,precio,comentario,comentario_eng,imagen FROM moviles WHERE nombre IN ('$nombre1','$nombre2') LIMIT 2";
        mysql_query('SET CHARACTER SET utf8');
	$res = mysql_query($query,$link) or die('Errant query:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
			for ($i=0; $i<=23; $i++){
				unset($row[$i]);
			}
			
			/* Realizamos las consultas para saber la puntuacion */
			$total=0;
			
			/* Procesador */
			$query = "SELECT puntuacion FROM procesador WHERE nombre = '".$row['procesador']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* GPU */
			$query = "SELECT puntuacion FROM gpu WHERE nombre = '".$row['gpu']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* SO */
			$query = "SELECT puntuacion FROM so WHERE nombre = '".$row['so']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Pantalla */
			$query = "SELECT puntuacion FROM pantalla WHERE nombre = '".$row['pantalla']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Proteccion Pantalla */
			$query = "SELECT puntuacion FROM proteccion_pantalla WHERE nombre = '".$row['proteccion']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Camara */
			$query = "SELECT puntuacion FROM camara WHERE nombre = '".$row['camara']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Sacamos el resto de la puntuacion y formateamos los datos de salida a gusto */
			$total += ($row['ram']/1.5) + ($row['memoria']/100) + $row['card_slot'] + ($row['bateria']/1.5) + ($row['tamano_pantalla']*230) + $row['conectividad'];
			$res_tokens = explode (" ",$row['res_pantalla']);
			$punt_res = ($res_tokens[0] + $res_tokens[2])/3;
			$total += $punt_res + $row['nfc'] + $row['gps'] + $row['radio'];
			$row['gpu'] = str_replace("Integrada","Built-in",$row['gpu']);
			$row['ram'] = $row['ram'] . " MB";
			$row['memoria'] = ($row['memoria']/1000) . " GB";
			if ($row['card_slot']>0) {
				if ($lang == "es")	$row['card_slot'] = "Sí";
				else	$row['card_slot'] = "Yes";
			}
			else	$row['card_slot'] = "No";
			$row['bateria'] = $row['bateria'] . " mAh";
			if ($lang == "es")
				$row['tamano_pantalla'] = $row['tamano_pantalla'] . " pulgadas";
			else
				$row['tamano_pantalla'] = $row['tamano_pantalla'] . " inches";
			$row['res_pantalla'] = $row['res_pantalla'] . " pxs";
			if (!isset($row['proteccion']))	$row['proteccion'] = "No";
			if ($lang != "es")	$row['proteccion'] = str_replace(" y "," and ",$row['proteccion']);
			if (is_null($row['camara']))	$row['camara'] = "-";
			if ($lang != "es") {
				$spanish = array(" y ", " con ", "zoom óptico", "delantera", "trasera");
				$english   = array(" and ", " with ", "optical zoom", "front", "rear");
				$row['camara'] = str_replace($spanish, $english, $row['camara']);
			}
			if ($row['conectividad']=="330") {
				if ($lang == "es")	$row['conectividad']="Bluetooth y Wifi";
				else	$row['conectividad']="Bluetooth and Wifi";
			}	else if ($row['conectividad']=="333")	{
				if ($lang == "es")	$row['conectividad']="Bluetooth, Wifi e IR";
				else	$row['conectividad']="Bluetooth, Wifi and IR";
			}
			else $row['conectividad']="Wifi";
			if ($row['nfc']>250) {
				if ($lang == "es")	$row['nfc'] = "Sí";
				else	$row['nfc'] = "Yes";
			} else {
				if ($row['nfc']==250) {
					if ($lang == "es")	$row['nfc'] = "Opcional";
					else	$row['nfc'] = "Optional";
				}
				else	$row['nfc'] = "No";
			}
			if ($row['gps']>0) {
				if ($lang == "es")	$row['gps'] = "Sí";
				else	$row['gps'] = "Yes";
			}
			else	$row['gps'] = "No";
			if ($row['radio']>0) {
				if ($lang == "es")	$row['radio'] = "Sí";
				else	$row['radio'] = "Yes";
			}
			else	$row['radio'] = "No";
			$tokens = explode(" x ", $row['medidas']);
			$total -= ($tokens[0]+$tokens[1]+$tokens[2]);
			//$total -= $row['peso'] + $row['precio'];
			$total -= $row['peso'];
			if (is_null($row['medidas'])) {
				$row['medidas'] = "-";
			} else {
				if ($lang == "en")
					$row['medidas'] = number_format($tokens[0]/25.4,2)." in X ".number_format($tokens[1]/25.4,2)." in X ".number_format($tokens[2]/25.4,2)." in";
				else
					$row['medidas'] = $tokens[0]." mm X ".$tokens[1]." mm X ".$tokens[2]." mm";
			}
			if (is_null($row['peso'])) {
				$row['peso'] = "-";
			} else {
				if ($lang == "en")
					$row['peso'] = number_format($row['peso']/28.349,2) . " oz";
				else
					$row['peso'] = $row['peso'] . " gr.";
			}
			if ($row['precio']=="0" ) {
				if ($lang == "es")	$row['precio'] = "Descatalogado.";
				else	$row['precio'] = "No longer stocked.";
			} else if (!isset($row['precio'])) {
				if ($lang == "es")	$row['precio'] = "Pendiente de conocer.";
				else	$row['precio'] = "Not yet known.";
			} else {
				if ($moneda == "euro")	$row['precio'] = $row['precio'] . " euros";
				else if ($moneda == "dolar")	$row['precio'] = round($row['precio']/$conversion_dollar) . " dollars";
				else if ($moneda == "libra")	$row['precio'] = round($row['precio']/$conversion_pound) . " pounds";
				else if ($moneda == "rupia")	$row['precio'] = round($row['precio']/$conversion_rupia) . " rupees";
				else	$row['precio'] = round($row['precio']/$conversion_peso) . " pesos";
				if ($lang == "es")	$row['precio'] = $row['precio'] . " (aprox.)";
				else	$row['precio'] = $row['precio'] . " (approx.)";
			}
			if ($lang != "es")	$row['comentario'] = $row['comentario_eng'];
			if (is_null($row['comentario']))  $row['comentario'] = "N/A";
			else { if($row['comentario']=="")	$row['comentario'] = "N/A"; }
			if (is_null($row['imagen']))  $row['imagen'] = "no";
			else { if($row['imagen']=="")	$row['imagen'] = "no"; }
			
			$row['puntuacion_total']=number_format($total,2,'.','');
			
			//Devolvemos el resultado
			$resultado[] = $row;
		}
		
		if ($resultado[0]['nombre'] != $nombre1) {
			$aux[0] = $resultado[1];
			$aux[1] = $resultado[0];
			$resultado = $aux;
		}
		
		$resultado[]['resta_total'] = number_format($resultado[0]['puntuacion_total'] - $resultado[1]['puntuacion_total'],2);
		if ($resultado[2]['resta_total']>0)	$resultado[2]['ganador'] = $resultado[0]['nombre'];
		else	$resultado[2]['ganador'] = $resultado[1]['nombre'] ;
	}
 
    /* desconectamos de la DB */
	@mysql_close($link);

	return $resultado;
}

/* Funcion para sacar moviles entre un rango de precios */
function movil_rango_precios($lang = "en", $moneda = "euro") {
	global $conversion_dollar, $conversion_peso, $conversion_rupia, $conversion_pound;

	/* Conectamos a la BD */
    $link=conectarBD();

	/* hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT nombre,precio FROM moviles ORDER BY precio,nombre";
	$res = mysql_query($query,$link) or die('Query error:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
           while ($row= mysql_fetch_array($res)) {
				/*if ($row['precio']=="0")	$resultado[]= $row['nombre']." - Descatalogado";
				else if (!isset($row['precio']))	$resultado[]= $row['nombre']." - Pendiente de conocer";
				else	$resultado[]= $row['nombre']." - ".$row['precio']." € aprox.";*/
				
				if ($row['precio']=="0" ) {
					if ($lang == "es")	$resultado[] = $row['nombre']." - Descatalogado.";
					else	$resultado[] = $row['nombre']." - No longer stocked.";
				} else if (!isset($row['precio'])) {
					if ($lang == "es")	$resultado[] = $row['nombre']." - Pendiente de conocer.";
					else	$resultado[] = $row['nombre']." - Not yet known.";
				} else {
					if ($moneda == "euro")
						$resultado[] = $row['nombre']." - ".$row['precio'] . " euros";
					else if ($moneda == "dolar")
						$resultado[] = $row['nombre']." - ".round($row['precio']/$conversion_dollar) . " dollars";
					else if ($moneda == "libra")
						$resultado[] = $row['nombre']." - ".round($row['precio']/$conversion_pound) . " pounds";
					else if ($moneda == "rupia")
						$resultado[] = $row['nombre']." - ".round($row['precio']/$conversion_rupia) . " rupees";
					else
						$resultado[] = $row['nombre']." - ".round($row['precio']/$conversion_peso) . " pesos";
				}
           }
	}

    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
}

/* Funcion para sacar moviles segun el sistema operativo */
function movil_so() {

	/* Conectamos a la BD */
    $link=conectarBD();

	/* hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT nombre,so FROM moviles ORDER BY so,nombre";
	$res = mysql_query($query,$link) or die('Query error:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
           while ($row= mysql_fetch_array($res)) {
				$resultado[]= $row['nombre']." - ".$row['so'];
           }
	}

    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
}

//Funcion para sacar los moviles por ranking de puntuacion
function ranking() {
	
	/* Conectamos a la BD */
	$link=conectarBD();
	
	/* hacemos la consulta global para sacar los datos de cada movil */
	$query = "SELECT id,nombre,procesador,gpu,ram,memoria,card_slot,so,bateria,pantalla,res_pantalla,tamano_pantalla,proteccion,camara,conectividad,nfc,gps,radio,medidas,peso,precio FROM moviles ORDER BY id";
	$res = mysql_query($query,$link) or die('Errant query:  '.$query);

	/* Creamos un array global de los resultados */
	$result = $resultado = array();
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
			for ($i=0; $i<=20; $i++){
				unset($row[$i]);
			}
			
			/* Realizamos las consultas para saber la puntuacion */
			$total=0;
			
			/* Procesador */
			$query = "SELECT puntuacion FROM procesador WHERE nombre = '".$row['procesador']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* GPU */
			$query = "SELECT puntuacion FROM gpu WHERE nombre = '".$row['gpu']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* SO */
			$query = "SELECT puntuacion FROM so WHERE nombre = '".$row['so']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Pantalla */
			$query = "SELECT puntuacion FROM pantalla WHERE nombre = '".$row['pantalla']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Proteccion Pantalla */
			$query = "SELECT puntuacion FROM proteccion_pantalla WHERE nombre = '".$row['proteccion']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Camara */
			$query = "SELECT puntuacion FROM camara WHERE nombre = '".$row['camara']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Sacamos el resto de la puntuacion y formateamos los datos de salida a gusto */
			$total += ($row['ram']/1.5) + ($row['memoria']/100) + $row['card_slot'] + ($row['bateria']/1.5) + ($row['tamano_pantalla']*230) + $row['conectividad'];
			$res_tokens = explode (" ",$row['res_pantalla']);
			$punt_res = ($res_tokens[0] + $res_tokens[2])/3;
			$total += $punt_res + $row['nfc'] + $row['gps'] + $row['radio'];
			$tokens = explode(" x ", $row['medidas']);
			$total -= ($tokens[0]+$tokens[1]+$tokens[2]);
			//$total -= $row['peso'] + $row['precio'];
			$total -= $row['peso'];
			
			$row['puntuacion_total']=number_format($total,2,'.','');
			
			//Devolvemos el resultado
			do {
				if (isset($result[$row['puntuacion_total']]))	$row['puntuacion_total'] = $row['puntuacion_total'] + 1;
			} while (isset($result[$row['puntuacion_total']]));
			$result[$row['puntuacion_total']] = $row['nombre']." - ".number_format($row['puntuacion_total'],2,'.','');
		}
		
		ksort($result, SORT_NUMERIC);
		$result = array_reverse($result,true);
		foreach ($result as $block) {
			$resultado[]=$block;
		}
	}
 
    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
	
}

//Funcion para sacar listado de relacion calidad/precio
function calidad_precio() {
    
	/* Conectamos a la BD */
	$link=conectarBD();
	
	/* hacemos la consulta global para sacar los datos de cada movil */
	$query = "SELECT id,nombre,procesador,gpu,ram,memoria,card_slot,so,bateria,pantalla,res_pantalla,tamano_pantalla,proteccion,camara,conectividad,nfc,gps,radio,medidas,peso,precio FROM moviles ORDER BY id";
	$res = mysql_query($query,$link) or die('Errant query:  '.$query);

	/* Creamos un array global de los resultados */
	$result = $resultado = array();
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
			for ($i=0; $i<=20; $i++){
				unset($row[$i]);
			}
			
			/* Realizamos las consultas para saber la puntuacion */
			$total=0;
			
			/* Procesador */
			$query = "SELECT puntuacion FROM procesador WHERE nombre = '".$row['procesador']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* GPU */
			$query = "SELECT puntuacion FROM gpu WHERE nombre = '".$row['gpu']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* SO */
			$query = "SELECT puntuacion FROM so WHERE nombre = '".$row['so']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Pantalla */
			$query = "SELECT puntuacion FROM pantalla WHERE nombre = '".$row['pantalla']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Proteccion Pantalla */
			$query = "SELECT puntuacion FROM proteccion_pantalla WHERE nombre = '".$row['proteccion']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Camara */
			$query = "SELECT puntuacion FROM camara WHERE nombre = '".$row['camara']."' LIMIT 1";
			$respunt = mysql_query($query,$link) or die('Errant query:  '.$query);
			if(mysql_num_rows($respunt)) {
				$rowpunt = mysql_fetch_array($respunt);
				$total += $rowpunt["puntuacion"];
			}
			
			/* Sacamos el resto de la puntuacion y formateamos los datos de salida a gusto */
			$total += ($row['ram']/1.5) + ($row['memoria']/100) + $row['card_slot'] + ($row['bateria']/1.5) + ($row['tamano_pantalla']*230) + $row['conectividad'];
			$res_tokens = explode (" ",$row['res_pantalla']);
			$punt_res = ($res_tokens[0] + $res_tokens[2])/3;
			$total += $punt_res + $row['nfc'] + $row['gps'] + $row['radio'];
			$tokens = explode(" x ", $row['medidas']);
			$total -= ($tokens[0]+$tokens[1]+$tokens[2]);
			//$total -= $row['peso'] + $row['precio'];
			$total -= $row['peso'];
			
            if (isset($row['precio']) && ($row['precio'] != 0)) {
                $total = $total/$row['precio'];
                
    			$row['puntuacion_total']=number_format($total,2,'.','');
    			
    			//Devolvemos el resultado
    			do {
    				if (isset($result[$row['puntuacion_total']]))	$row['puntuacion_total'] = $row['puntuacion_total'] + 1;
    			} while (isset($result[$row['puntuacion_total']]));
    			$result[$row['puntuacion_total']] = $row['nombre']." - ".number_format($row['puntuacion_total'],2,'.','');
            }
		}
		
		ksort($result, SORT_NUMERIC);
		$result = array_reverse($result,true);
		foreach ($result as $block) {
			$resultado[]=$block;
		}
	}
 
    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
	
}

/* Funcion para sacar moviles con su id */
function movil_id() {

	/* Conectamos a la BD */
    $link=conectarBD();

	/* hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT id,nombre FROM moviles ORDER BY id";
	$res = mysql_query($query,$link) or die('Query error:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
           while ($row= mysql_fetch_array($res)) {
				$resultado[]= $row['id']." - ".$row['nombre'];
           }
	}

    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
}

/* Funcion para filtrar moviles por parametros */
function filtro() {
    // Obtenemos valores de los filtros
    $cpu=isset($_GET['cpu']) ? $_GET['cpu'] : null;
    $gpu=isset($_GET['gpu']) ? $_GET['gpu'] : null;
    $ram=isset($_GET['ram']) ? $_GET['ram'] : null;
    $mem=isset($_GET['mem']) ? $_GET['mem'] : null;
    $cs=isset($_GET['cs']) ? $_GET['cs'] : null;
    $so=isset($_GET['so']) ? $_GET['so'] : null;
    $bateria=isset($_GET['bateria']) ? $_GET['bateria'] : null;
    $pantalla=isset($_GET['pantalla']) ? $_GET['pantalla'] : null;
    $tpant=isset($_GET['tpant']) ? $_GET['tpant'] : null;
    $respantalla=isset($_GET['respantalla']) ? $_GET['respantalla'] : null;
    $proteccion=isset($_GET['proteccion']) ? $_GET['proteccion'] : null;
    $camara=isset($_GET['camara']) ? $_GET['camara'] : null;
    $conectividad=isset($_GET['conectividad']) ? $_GET['conectividad'] : null;
    $nfc=isset($_GET['nfc']) ? $_GET['nfc'] : null;
    $gps=isset($_GET['gps']) ? $_GET['gps'] : null;
    $radio=isset($_GET['radio']) ? $_GET['radio'] : null;
    $precio=isset($_GET['precio']) ? $_GET['precio'] : null;
    
    /* Conectamos a la BD */
    $link=conectarBD();

    /* Hacemos la consulta para sacar todos los moviles existentes en la DB */
	$query = "SELECT nombre FROM moviles";
    $params = "";
    if (!is_null($cpu) && $cpu != "")
        $params .= " procesador = '$cpu'";
    if (!is_null($gpu) && $gpu != "") {
        if ($params != "")
            $params .= " AND";
        $params .= " gpu = '$gpu'";
    }
    if (!is_null($ram) && $ram != "_") {
        if ($params != "")
            $params .= " AND";
        $partes = explode("_",$ram);
        if ($partes[0] == "")   $partes[0] = 0;
        if ($partes[1] == "")   $partes[1] = 99999999;
        $params .= " ram >= ".$partes[0]." AND ram <= ".$partes[1];
    }
    if (!is_null($mem) && $mem != "_") {
        if ($params != "")
            $params .= " AND";
        $partes = explode("_",$mem);
        if ($partes[0] == "")   $partes[0] = 0;
        if ($partes[1] == "")   $partes[1] = 99999999;
        $params .= " memoria >= ".$partes[0]." AND memoria <= ".$partes[1];
    }
    if (!is_null($cs) && $cs != "") {
        if ($params != "")
            $params .= " AND";
        if ($cs != "No")
            $params .= " card_slot > 0";
        else
            $params .= " card_slot = 0";
    }
    if (!is_null($so) && $so != "") {
        if ($params != "")
            $params .= " AND";
        $params .= " so = '$so'";
    }
    if (!is_null($bateria) && $bateria != "_") {
        if ($params != "")
            $params .= " AND";
        $partes = explode("_",$bateria);
        if ($partes[0] == "")   $partes[0] = 0;
        if ($partes[1] == "")   $partes[1] = 99999999;
        $params .= " bateria >= ".$partes[0]." AND bateria <= ".$partes[1];
    }
    if (!is_null($pantalla) && $pantalla != "") {
        if ($params != "")
            $params .= " AND";
        $params .= " pantalla = '$pantalla'";
    }
    if (!is_null($tpant) && $tpant != "_") {
        if ($params != "")
            $params .= " AND";
        $partes = explode("_",$tpant);
        if ($partes[0] == "")   $partes[0] = 0;
        if ($partes[1] == "")   $partes[1] = 99999999;
        $params .= " tamano_pantalla >= ".$partes[0]." AND tamano_pantalla <= ".$partes[1];
    }
    if (!is_null($respantalla) && $respantalla != "") {
        if ($params != "")
            $params .= " AND";
        $params .= " res_pantalla = '$respantalla'";
    }
    if (!is_null($proteccion) && $proteccion != "") {
        if ($params != "")
            $params .= " AND";
        $params .= " proteccion = '$proteccion'";
    }
    if (!is_null($camara) && $camara != "") {
        if ($params != "")
            $params .= " AND";
        $params .= " camara = '$camara'";
    }
    if (!is_null($conectividad) && $conectividad != "") {
        if ($params != "")
            $params .= " AND";
        $params .= " conectividad >=".$conectividad;
    }
    if (!is_null($nfc) && $nfc != "") {
        if ($params != "")
            $params .= " AND";
        if ($nfc != "No")
            $params .= " nfc > 0";
        else
            $params .= " nfc = 0";
    }
    if (!is_null($gps) && $gps != "") {
        if ($params != "")
            $params .= " AND";
        if ($gps != "No")
            $params .= " gps > 0";
        else
            $params .= " gps = 0";
    }
    if (!is_null($radio) && $radio != "") {
        if ($params != "")
            $params .= " AND";
        if ($radio != "No")
            $params .= " radio > 0";
        else
            $params .= " radio = 0";
    }
    if (!is_null($precio) && $precio != "_") {
        if ($params != "")
            $params .= " AND";
        $partes = explode("_",$precio);
        if ($partes[0] == "")   $partes[0] = 0;
        if ($partes[1] == "")   $partes[1] = 99999999;
        $params .= " precio >= ".$partes[0]." AND precio <= ".$partes[1];
    }
        
    if ($params != "")
        $query .= " WHERE".$params;
        
    $query .= " ORDER BY id DESC";
    //print($query);
	$res = mysql_query($query,$link) or die('Query error:  '.$query);

	/* Creamos un array global de los resultados */
	$resultado = array();
	if(mysql_num_rows($res)) {
           while ($row= mysql_fetch_array($res)) {
		$resultado[]= $row['nombre'];
           }
	}

    /* desconectamos de la DB */
	@mysql_close($link);

	return (array("nombres" => $resultado));
}

/* Funcion para obtener los maestros */
function get_maestros($lang = "en") {
    
    /* Traduccion global */
    if ($lang == "es")
        $todo = "Todo";
    else
        $todo = "All";
    
    /* Conectamos a la BD */
    $link=conectarBD();

    /* hacemos la consulta para sacar todos los procesadores existentes en la DB */
    $query = "SELECT nombre FROM procesador ORDER BY nombre";
    $res = mysql_query($query,$link) or die('Errant query:  '.$query);

	/* Creamos un array global de los resultados */
	$result = array();
    
    /* Agregamos resultados a un array global */
    $result["procesador"][] = $todo;
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
            $result["procesador"][] = $row["nombre"];
		}
	}
    
    /* hacemos la consulta para sacar todos las gpus existentes en la DB */
    $query = "SELECT nombre FROM gpu ORDER BY nombre";
    $res = mysql_query($query,$link) or die('Errant query:  '.$query);

    /* Agregamos resultados a un array global */
    $result["gpu"][] = $todo;
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
            $result["gpu"][] = $row["nombre"];
		}
	}
    
    /* hacemos la consulta para sacar todas las camaras existentes en la DB */
    $query = "SELECT nombre FROM camara ORDER BY puntuacion DESC";
    $res = mysql_query($query,$link) or die('Errant query:  '.$query);

    /* Agregamos resultados a un array global */
    $result["camara"][] = $todo;
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
            if ($lang != "es") {
        		$spanish = array(" y ", " con ", "zoom óptico", "delantera", "trasera");
    			$english   = array(" and ", " with ", "optical zoom", "front", "rear");
    			$row["nombre"] = str_replace($spanish, $english, $row["nombre"]);
    		}
            $result["camara"][] = $row["nombre"];
		}
	}
    
    /* hacemos la consulta para sacar todas las pantallas existentes en la DB */
    $query = "SELECT nombre FROM pantalla ORDER BY nombre";
    $res = mysql_query($query,$link) or die('Errant query:  '.$query);

    /* Agregamos resultados a un array global */
    $result["pantalla"][] = $todo;
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
            $result["pantalla"][] = $row["nombre"];
		}
	}
    
    /* hacemos la consulta para sacar todos las protecciones existentes en la DB */
    $query = "SELECT nombre FROM proteccion_pantalla ORDER BY nombre";
    $res = mysql_query($query,$link) or die('Errant query:  '.$query);

    /* Agregamos resultados a un array global */
    $result["proteccion_pantalla"][] = $todo;
	if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
            if ($lang != "es")    $row["nombre"] = str_replace(" y "," and ",$row["nombre"]);
            $result["proteccion_pantalla"][] = $row["nombre"];
		}
	}
    
    /* hacemos la consulta para sacar todos los SO existentes en la DB */
    $query = "SELECT nombre FROM so ORDER BY nombre";
    $res = mysql_query($query,$link) or die('Errant query:  '.$query);

    /* Agregamos resultados a un array global */
    $result["so"][] = $todo;
    if(mysql_num_rows($res)) {
		while($row = mysql_fetch_array($res)) {
            $result["so"][] = $row["nombre"];
		}
	}
    
    /* hacemos la consulta para sacar todas las resoluciones existentes en la DB */
    $query = "SELECT DISTINCT res_pantalla FROM moviles ORDER BY res_pantalla";
    $res = mysql_query($query,$link) or die('Errant query:  '.$query);

    /* Agregamos resultados a un array global */
    $result["res_pantalla"][] = $todo;
    if(mysql_num_rows($res)) {
        while($row = mysql_fetch_array($res)) {
            $result["res_pantalla"][] = $row["res_pantalla"];
		}
	}
    
    /* desconectamos de la DB */
    @mysql_close($link);
    
    return $result;
}

/* FUNCIONES AUXILIARES */

function conectarBD() {
        /* Conectamos a la base de datos */	
	$link = mysql_connect("khusu.db.8639285.hostedresource.com","khusu","Carlos@88") OR DIE ('Tareas de mantenimiento. Pruebe más tarde, por favor.');
	mysql_select_db('khusu',$link) or die('Cannot select the DB');
        return $link;
}

?>