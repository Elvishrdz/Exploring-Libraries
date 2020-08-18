package com.eahm.exploringlibraries.models

import com.google.gson.annotations.SerializedName

/**
 * 3. Use GSON to specify where to set the value of "answer_id" and "is_accepted" (from the JSON
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
        "owner": {
            "reputation": 63619,
            "user_id": 176761,
            "user_type": "registered",
            "accept_rate": 100,
            "profile_image": "https://www.gravatar.com/avatar/27d3dc0518cc36409d91b9c3a1eb9231?s=128&d=identicon&r=PG",
            "display_name": "Alex Volovoy",
            "link": "https://stackoverflow.com/users/176761/alex-volovoy"
        },
        "is_accepted": true,
        "community_owned_date": 1386913705,
        "score": 5813,
        "last_activity_date": 1586973970,
        "last_edit_date": 1586973970,
        "creation_date": 1262925713,
        "answer_id": 2025541,
        "question_id": 2025282,
        "content_license": "CC BY-SA 4.0"
    }
 */
data class Answer(
    val owner : Owner = Owner(),
    @SerializedName("answer_id") val answerId : Int,
    @SerializedName("is_accepted") val isAccepted : Boolean,
    val score : Int
){
    override fun toString(): String {
        return "ID: $answerId - Score: $score - Accepted: ${if(isAccepted) "Yes" else "No"}"
    }
}

data class Owner(
    @SerializedName("display_name") val displayName : String = "",
    @SerializedName("accept_rate") val acceptRate : Int = 0
){
    override fun toString(): String {
        return "Answer Owner:\nName: ${displayName}\nAccept Rate: $acceptRate"
    }
}
