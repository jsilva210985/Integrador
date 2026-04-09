package com.xcustom.alan.businessrules;

public enum BusinessRuleMessages {

	NO_PERMISSION_TERRESTRE(1,"No tienes permisos para crear guías de servicio terrestre. Consulta a tu vendedor para habilitar esta función."),
	NO_PERMISSION_EXPRESS(2,"No tienes permisos para crear guías de servicio express. Consulta a tu vendedor."),
	NOT_SUPPORTED(3,"Servicio no soportado. Consulta a tu vendedor."),
	TYPE_OF_PAYMENT_NOT_FOUND(4,"Tipo de cobro no encontrado. Consulta a tu vendedor."),
	WEIGHT_REQUIRED(5,"Peso es requerido y no puede ser vacio."),
	WEIGHT_INVALID_FORAMT(6,"Formato de peso invalido, valor numero es esperado (ej: 0.5, 2, 3.75)"),
	USER_RANGES_NOT_FOUND(7,"No se encontro configuracion de rangos. Consulta a tu vendedor."),
	USER_RANGES_NOT_FOUND_VALUE(8,"No se encontro valor de rango en la configuracion para el peso enviado. Consulta a tu vendedor."),
	USER_NO_PERMISSION(9,"No tienes permiso de crear guias para el peso proporcionado. Consulta a tu vendedor."),

	USER_NO_PERMISSION_WEIGHT(10, "No tienes permisos para generar guías de servicio %s para paquetes de %d kg. Contacta a tu vendedor."),
	USER_RANGES_NOT_CONFIGURED(11,"La configuración de rangos de peso para el servicio %s está vacía o no es válida. Consulta con tu vendedor."),
	USER_WEIGHT_OUT_OF_RANGE(12,"El peso enviado (%d kg) no está dentro de los rangos permitidos para el servicio %s. Consulta a tu vendedor."),

	WEIGHT_NOT_ALLOWED_ZERO(13,"El peso enviado (0 kg) no es válido. Debe ser mayor a 0. Consulta a tu vendedor para conocer los rangos permitidos."),
	WEIGHT_NOT_ALLOWED_MAX(14,"El peso enviado (70 kg) excede el máximo permitido para generar guías. Consulta a tu vendedor para conocer los rangos permitidos.")

	;

	private final int code;
	private final String message;

	BusinessRuleMessages(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
