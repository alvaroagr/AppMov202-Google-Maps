# AppMov202-Google-Maps
## Información
Este repositorio contiene mi desarrollo del Reto 1: Google Maps de la clase de Aplicaciones Moviles del segundo semestre de 2020, dirigida por el profesor Domiciano 
Rincón.

La aplicación como tal es una implementación del API de Google Maps y los conocimientos que se han adquirido del uso de Activities, Fragments y HTTP Requests para 
crear una herramienta donde usuarios pueden ingresar con un nombre, transmitir sus coordenadas y recibir las de los demas, registrar las coordenadas de huecos y recibir
las coordenadas de huecos ingresados por otros.

La aplicación cumple con los siguientes requerimientos:
- [x] La cámara del mapa puede seguir el rastro del usuario, mostrando un marcador en su posición.
- [x] Al entrar a la aplicación el usuario debe poder ver los marcadores correspondientes a los demás usuarios conectados y a todos los huecos. Fije un tiempo razonable 
para la actualización de la posición de todos los marcadores. 
- [x] El usuario puede registrar un hueco. El modelo de hueco debe tener información suficiente para representar sus estados: No confirmado y confirmado.
- [x] El diálogo debe mostrar la dirección del hueco. 
- [x] El cajón inferior de la aplicación debe informar cuál es el hueco más cercano al usuario en metros. 

**IMPORTANTE:** Por obvias razones, los archivos 
* /app/src/debug/res/values/google_maps_api.xml
* /app/src/release/res/values/google_maps_api.xml

Poseen la siguiente linea:

```xml
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">YOUR_API_KEY_HERE</string>
```

El componente de Google Maps de la aplicación **no funcionara** a menos que usted incluya su propia clave de API (no muestro la mia por obvios motivos de seguridad), 
ubicandola en donde está `YOUR_API_KEY_HERE`.

Si el profesor desea una version donde si comparta dicha clave API, puedo hacer un commit tan pronto me contacte a traves de Slack o correo electronico.

**Alvaro A. Gómez Rey. 2020.**
