package com.eahm.exploringlibraries.utils

/**
 *
 * The StackOverflow API returns the JSON results in a root tag called "items".
 * To obtain both lists (Questions and Answers) we will use a generic list.
 *
 * This class accepts a type parameter and simply wraps a list of objects of that type.
 *
 */
class ListWrapper<T> {
    var items : List<T>? = null
}