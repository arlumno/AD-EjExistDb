for $prod in collection("/db/ColeccionesXML/BDProductosXML")/productos/produc
where $prod/precio>50 and $prod/cod_zona=10
return $prod 