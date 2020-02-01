<?php

function date($param){
    $d = getdate();
    $signature = $SERVER_SIGNATURE;
    return string(d) . $signature;
}

$server = new SoapServer("lastexo.wsdl");

$server->addFunction("date");
$server->handle();

?>
