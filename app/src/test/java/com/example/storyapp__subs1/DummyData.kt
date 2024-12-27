package com.example.storyapp__subs1

import com.example.storyapp__subs1.data.respons.ListStoryItem

object DummyData {
    fun generateDummyNewsEntity(): List<ListStoryItem> {
        val StoryList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val stories = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1734390634136_7ee44895993f5df8d5f4.jpg",
                "2022-02-22T22:22:22Z",
                "xcforce",
                "tes",
                3.1,
                "story-gMFUVN5JX8BXIeMX",
                5.3,
            )
            StoryList.add(stories)
        }
        return StoryList
    }
}