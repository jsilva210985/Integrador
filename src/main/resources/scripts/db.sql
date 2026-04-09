CREATE TABLE `guias_integrador` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  
  `remitente_calle` varchar(400)  NOT NULL default '',
  `remitente_colonia` varchar(400)  NOT NULL default '',
  `remitente_cp` varchar(400)  NOT NULL default '',
  `remitente_nombre` varchar(400)  NOT NULL default '',
  `remitente_estado` varchar(400)  NOT NULL default '',
  `remitente_municipio` varchar(400)  NOT NULL default '',
  `remitente_numero_exterior` varchar(400)  NOT NULL default '',
  `remitente_referencia` varchar(400)  NOT NULL default '',
  `remitente_telefono` varchar(400)  NOT NULL default '',
  
  `destinatario_calle` varchar(400)  NOT NULL default '',
  `destinatario_colonia` varchar(400)  NOT NULL default '',
  `destinatario_cp` varchar(400)  NOT NULL default '',
  `destinatario_nombre` varchar(400)  NOT NULL default '',
  `destinatario_estado` varchar(400)  NOT NULL default '',
  `destinatario_municipio` varchar(400)  NOT NULL default '',
  `destinatario_numero_exterior` varchar(400)  NOT NULL default '',
  `destinatario_referencia` varchar(400)  NOT NULL default '',
  `destinatario_telefono` varchar(400)  NOT NULL default '',
  
  `informacion_adicional` varchar(400)  NOT NULL default '',
  `tipo_guia` varchar(400)  NOT NULL default '',
  `contenido` varchar(400)  NOT NULL default '',
  `etiquetas` varchar(400)  NOT NULL default '',
  `tipo_contenido` varchar(400)  NOT NULL default '',
  `kilos` varchar(400)  NOT NULL default '',
  
  `cliente` varchar(400)  NOT NULL default '',
  `tracking` varchar(100)  NOT NULL default '',
  `estatus` varchar(1200)  NULL default '',
  
  PRIMARY KEY (`id`),
  KEY `id_index1` (`id`),
  KEY `cliente_index2` (`cliente`),
  KEY `tracking_index3` (`tracking`)
);


CREATE TABLE `tokens` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `token` varchar(1200)  NOT NULL default '',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_expiracion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  
  PRIMARY KEY (`id`),
  KEY `id_index1` (`id`),
  KEY `id_usuario_index2` (`id_usuario`)
);
