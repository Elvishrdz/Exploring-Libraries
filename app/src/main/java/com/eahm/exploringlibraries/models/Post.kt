package com.eahm.exploringlibraries.models

/**
 * 4. Our model must match the fields in the JSON response: *
 * [
 *   {
 *     "userId": 2,
 *     "id": 11,
 *     "title": "et ea vero quia laudantium autem",
 *     "body": "delectus reiciendis molestiae occaecati non minima eveniet qui voluptatibus\naccusamus in eum beatae sit\nvel qui neque voluptates ut commodi qui incidunt\nut animi commodi"
 *   }
 * ]
 */
data class Post(
    val id : Int,
    val title : String,
    val body : String
)