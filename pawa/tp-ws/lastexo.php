<?php

function instanceport($param){
    
}

$server = new SoapServer("helloworld.wsdl");

$server->addFunction("instanceport");
$server->handle();
?>
