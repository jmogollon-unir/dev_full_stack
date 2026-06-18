# Uso de IA - Catalogue con Elasticsearch

Para la actividad 3 se uso IA como apoyo en la migracion consultiva de `catalogue` hacia Elasticsearch.

## Prompt base usado

```text
Actua como ingeniero senior Java/Spring. Tengo un microservicio catalogue con Spring Boot, JPA, MySQL, Book, Genre, BookDto, SearchBooksCriteria y servicios CRUD. Necesito adaptarlo a la actividad 3 de UNIR: las consultas deben ejecutarse contra Elasticsearch, las escrituras contra MySQL y cada escritura debe sincronizar el indice. Genera el POJO de indice, repositorio Elasticsearch, servicio de indexacion y modifica los servicios existentes manteniendo la API REST actual.
```

## Resultado aplicado

- Se agrego `BookSearchDocument` como POJO del indice `books`.
- Se agrego `BookSearchRepository` con Spring Data Elasticsearch.
- Se agrego `BookSearchService` para resolver `GET /api/books` y `GET /api/books/{id}` desde Elasticsearch.
- Se agrego `BookSearchIndexer` para sincronizar el indice al crear, actualizar, eliminar y descontar stock.
- Se agrego reindexacion inicial desde MySQL al arrancar `catalogue`.

## Estimacion para video-memoria

- Respuestas correctas o parcialmente correctas: 80%.
- Respuestas incorrectas o que requirieron ajuste manual: 20%.
- Lineas aproximadas generadas o guiadas por IA: 230.
- Tiempo estimado ahorrado: 2 a 3 horas.
