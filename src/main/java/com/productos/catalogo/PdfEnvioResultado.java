package com.productos.catalogo;

public record PdfEnvioResultado (Long userId, String email, boolean ok, String rutaPdf, String error){

}
