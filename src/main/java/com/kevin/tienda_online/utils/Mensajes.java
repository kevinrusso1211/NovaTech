package com.kevin.tienda_online.utils;

public class Mensajes {

    private Mensajes(){}

    // Usuario
    public static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado";
    public static final String USUARIO_YA_EXISTE = "El usuario ya está registrado";
    public static final String CREDENCIALES_INVALIDAS = "Contraseña incorrecta";

    // Producto
    public static final String PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
    public static final String STOCK_INSUFICIENTE = "Stock insuficiente para el producto: ";

    // Carrito
    public static final String CARRITO_NO_ENCONTRADO = "Carrito no encontrado";
    public static final String CARRITO_VACIO = "Carrito vacio";
    public static final String PRODUCTO_NO_EXISTE_EN_CARRITO = "Producto no encontrado en el carrito";
    public static final String CANTIDAD_INVALIDA = "La cantidad debe ser mayor que cero";

    // Pedido
    public static final String PEDIDO_NO_ENCONTRADO = "Pedido no encontrado";
    public static final String ESTADO_PEDIDO_INVALIDO = "No es posible realizar ese cambio de estado";
    public static final String PEDIDO_YA_PAGADO = "El pedido ya fue pagado";
    public static final String PEDIDO_YA_CANCELADO = "El pedido ya fue cancelado";
    
    public static final String STOK_INSUFICIENTE = "Stock insuficiente";

    // Favorito
    public static final String FAVORITO_YA_EXISTE = "El producto ya está en favoritos";
    public static final String FAVORITO_NO_ENCONTRADO = "El producto no está en favoritos";

    //RESEÑAS
    public static final String RESEÑA_NO_ENCONTRADA = "Reseña no encontrada";
    public static final String RESEÑA_YA_EXISTE = "El usuario ya ha reseñado este producto";
    public static final String COMPRA_NO_VERIFICADA = "Solo puedes reseñar productos que hayas comprado";
    public static final String CALIFICACION_INVALIDA = "La calificación debe estar entre 1 y 5";
    public static final String CALIFICACION_OBLIGATORIA = "La calificación es obligatoria.";
    public static final String CALIFICACION_MINIMA = "La calificación mínima es 1.";
    public static final String CALIFICACION_MAXIMA = "La calificación máxima es 5.";
    public static final String COMENTARIO_OBLIGATORIO = "El comentario es obligatorio.";
    public static final String COMENTARIO_MAXIMO = "El comentario no puede superar los 500 caracteres.";

    //CUPONES
    public static final String CODIGO_OBLIGATORIO = "El código del cupón es obligatorio.";
    public static final String DESCUENTO_OBLIGATORIO = "El porcentaje de descuento es obligatorio.";
    public static final String DESCUENTO_MINIMO = "El descuento mínimo es 1%.";
    public static final String DESCUENTO_MAXIMO = "El descuento máximo es 100%.";
    public static final String FECHA_INICIO_OBLIGATORIA = "La fecha de inicio es obligatoria.";
    public static final String FECHA_FIN_OBLIGATORIA = "La fecha de fin es obligatoria.";
    public static final String MAXIMO_USOS_OBLIGATORIO = "El máximo de usos es obligatorio.";
    public static final String MAXIMO_USOS_MINIMO = "El máximo de usos debe ser mayor que cero.";
    public static final String CUPON_YA_EXISTE = "Ya existe un cupón con ese código.";
    public static final String CUPON_NO_ENCONTRADO = "El cupón no fue encontrado.";
    public static final String CUPON_INACTIVO = "El cupón SE ENCUENTRA INACTIVO.";
    public static final String CUPON_EXPIRADO = "El cupón ya expiró o aún no se encuentra vigente.";
    public static final String CUPON_SIN_USOS = "El cupón alcanzó el maximo de usos posibles.";


}
