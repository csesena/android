<?php
ini_set('error_reporting', E_ALL);
ini_set('display_errors', 1);

if (isset($_REQUEST['imagefile'])) $imageFile=$_REQUEST['imagefile'];
else $imageFile="improd/no_imagen.jpg";

$imageFile="http://marketplace.sanchezsesena.com/improd/".$imageFile;

if (exif_imagetype($imageFile) == IMAGETYPE_JPEG)	$imageObject = imagecreatefromjpeg($imageFile);
if (exif_imagetype($imageFile) == IMAGETYPE_GIF)	$imageObject = imagecreatefromgif($imageFile);
if (exif_imagetype($imageFile) == IMAGETYPE_PNG)	$imageObject = imagecreatefrompng($imageFile);

if (isset($imageObject)) {
	$ruta=substr($imageFile,44);

	// Get new dimensions
	list($width, $height) = getimagesize($imageFile);	

	$new_image = imagecreatetruecolor(64, 64);
     	imagecopyresampled($new_image, $imageObject, 0, 0, 0, 0, 64, 64, $width, $height);
	imagepng($new_image, '/home/content/85/8639285/html/marketplace/Android/android_images/' . $ruta . '.png');
}
print ("OK");

?>
