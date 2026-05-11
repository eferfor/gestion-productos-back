package com.productos.catalogo;

import java.util.Map;

public record PdfEnvioResultado (Long userId, String email, boolean emailValid, boolean ok, String rutaPdf, String error){

}
