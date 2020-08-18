package com.eahm.exploringlibraries.models

import com.google.gson.annotations.SerializedName

/**
 * 3. Use GSON to specify where to set the value of "question_id" (from the JSON
 *    response) in our model using the @SerializedName annotation. This is helpful
 *    when the JSON name is different from our variable. Using the annotation we
 *    can tell Retrofit where to set that value in our model.
 *
 *** The StackOverflow API response has many field. For this example we will use just
 *   some of them.
 */
/**
 * JSON response example:
    {
        "tags": [
            "android",
            "android-layout",
            "user-interface",
            "dimension",
            "units-of-measurement"
        ],
        "owner": {
            "reputation": 62407,
            "user_id": 235885,
            "user_type": "registered",
            "accept_rate": 87,
            "profile_image": "https://www.gravatar.com/avatar/a246044d1113994fba38a67ce0205ed9?s=128&d=identicon&r=PG",
            "display_name": "capecrawler",
            "link": "https://stackoverflow.com/users/235885/capecrawler"
        },
        "is_answered": true,
        "view_count": 1271201,
        "protected_date": 1303421057,
        "accepted_answer_id": 2025541,
        "answer_count": 33,
        "score": 5874,
        "last_activity_date": 1587545593,
        "creation_date": 1262921026,
        "last_edit_date": 1543813695,
        "question_id": 2025282,
        "content_license": "CC BY-SA 4.0",
        "link": "https://stackoverflow.com/questions/2025282/what-is-the-difference-between-px-dip-dp-and-sp",
        "title": "What is the difference between &quot;px&quot;, &quot;dip&quot;, &quot;dp&quot; and &quot;sp&quot;?"
    }
 */
data class Question(
    val title : String,
    val body : String,
    @SerializedName("question_id") val questionId : String
){
    override fun toString(): String {
        return title
    }
}