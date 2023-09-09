**ACTIVIDAD DE APRENDIZAJE DE PROGRAMACION MULTIMEDIA Y DISPOSITIVOS MOVILES - 2ª Evaluación**

Se ha seguido con la app de la 1ª evaluación pero esta vez conectada a la **API** de mi AA de 
Acceso a Datos: https://github.com/Joserra2304/AA_API_Cybershop.git

La comunicación con la API se ha llevado a cabo a traves de **Retrofit**

Se ha utilizado el patrón **MVP** (Model-View-Presenter)

En cuanto a operaciones realizadas de cada clase:

* **GET**: Las tres clases tienen dos operaciones GET: una que muestra todos los registros (la lista habitual) 
y otro por ID que muestra los detalles de cada registro (el que seria el detailsactivity).
* **POST**: Las tres clases tienen una operacion POST cada uno para realizar un registro
* **DELETE**: En las tres clases se pueden borrar los registros mostrados
* **PUT**: En las tres clases se puede actualizar la información de cada registro

Las clase **Client** tiene nuevos atributos en la API como longitud, latitud
para el caso del mapa, un String de imagen, y un Boolean **favourite**, el cual, 
conectandola a la APP, desde la APP se puede marcar o desmarcar un cliente como **FAVORITO**

También se ha seguido una pauta importante durante la realización del proyecto a traves de
**GitHub**, utilizando comandos **Git** en la terminal, creando ramas especificas. Finalmente
mergeadas a la rama principal master. A continuación incluyo dicho enlace:
https://github.com/Joserra2304/AA_PMDM_EV2.git

Finalmente, en el apartado de Github de **Issues** he añadido algun problema que sucede en la APP, 
como por ejemplo, no aparece la imagen registrada del cliente o producto en los detalles del
registro, y a veces incluso en la pantalla de actualización.
