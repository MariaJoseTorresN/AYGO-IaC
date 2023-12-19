# APLICACIÓN WEB IMPLEMENTADA AUTOMÁTICAMENTE EN AWS

## Descripción
Para este trabajo se aprovecha el primer laboratorio para diseñar un script que aprovisione tres instancias ec2 con los recursos y comandos necesarios, con el fin de automatizar la creación de un servidor web simple. Para lograr esto, se usa cdk como herramienta de creación de infraestructura como código.

## Requisitos
* Maven - Administrador de dependencias
* Git - Sistema de control de versiones
* Java - Lenguaje orientado a objetos
* Amazon cdk - Amazon cloud development kit

## Comandos

 * `mvn clean install`    limpia e instala dependencias
 * `cdk synth`            genera la plantilla de CloudFormation
 * `cdk deploy`           despliega el stack en la cuenta por defecto

## Instrucciones

1. Clonar repositorio y configurar credenciales propias.
2. Ejecutar comando `mvn clean install`
3. Ejecutar comando `cdk synth` para generar la plantilla o `cdk deploy` para desplegar el stack en tu cuenta de aws.
4. Esperar que el stack se despliegue, consultando en Cloudformation.
5. Acceder al endpoint /hello de cada maquina creada para verificar el funcionamiento.

    **Nota:** Al terminar, no olvides borrar el stack de cloudformation.