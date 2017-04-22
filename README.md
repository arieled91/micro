## Micro Compiler es un compilador para el lenguaje de programacion Micro

### DESCRIPCIÓN
Fischer presenta un lenguaje de programación que denomina Micro. Es un lenguaje muy simple que está diseñado,
específicamente, para poseer un lenguaje de programación concreto sobre el que se pueda analizar la construcción de un
compilador básico.
Informalmente, Fischer lo define de esta manera:

El único tipo de dato es entero.
Todos los identificadores son declarados implícitamente y con una
longitud máxima de 32 caracteres.
- Los identificadores deben comenzar con una letra y están compuestos
de letras y dígitos.
- Las constantes son secuencias de dígitos (números enteros).
- Hay dos tipos de sentencias:
Asignación
  ID := Expresión;
  Expresión es infija y se construye con identificadores,
  constantes y los operadores + y –; los paréntesis están
  permitidos.
Entrada/Salida
  read (lista de IDs);
  write (lista de Expresiones);

Cada sentencia termina con un "punto y coma" (;). El cuerpo de un
programa está delimitado por begin y end.
- begin, end, read y write son palabras reservadas y deben
escribirse en minúscula.

### Gramática Léxica
```
<token> -> uno de <identificador> <constante> <palabraReservada>
          <operadorAditivo> <asignación> <carácterPuntuación>
<identificador> -> <letra> {<letra o dígito>}
<constante> -> <dígito> {dígito>}
<letra o dígito> -> uno de <letra> <dígito>
<letra> -> una de a-z A-Z
<dígito> -> uno de 0-9
<palabraReservada> -> una de begin end read write
<operadorAditivo> -> uno de + -
<asignación> -> :=
<carácterPuntuación> -> uno de ( ) , ;
```
### Gramática Sintáctica
```
<programa> -> inicio <listaSentencias> fin
<listaSentencias> -> <sentencia> {<sentencia>}
<sentencia> -> <identificador> := <expresión> ; |
                leer ( <listaIdentificadores> ) ; |
                escribir ( <listaExpresiones> ) ;
<listaIdentificadores> -> <identificador> {, <identificador>}
<listaExpresiones> -> <expresión> {, <expresión>}
<expresión> -> <primaria> {<operadorAditivo> <primaria>}
<primaria> -> <identificador> | <constante> |
              ( <expresión> )
```

### Ejemplos:
```
begin
  read(a,b);
  a := a+11;
  b := b-1;
  write(a,b);
end
```
```
begin
  read (a,b);
  cc := a + (b-2);
  write (cc, a+4);
end
```
