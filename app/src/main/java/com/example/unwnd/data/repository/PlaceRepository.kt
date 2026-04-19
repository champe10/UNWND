package com.example.unwnd.data.repository

import com.example.unwnd.data.model.OpeningHour
import com.example.unwnd.data.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class PlaceRepository {
    private val _places = MutableStateFlow(mockPlaces)
    val places: Flow<List<Place>> = _places.asStateFlow()

    fun getPlaceById(id: String): Flow<Place?> {
        return _places.map { it.find { place -> place.id == id } }
    }

    fun toggleFavorite(id: String) {
        val currentList = _places.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            val place = currentList[index]
            currentList[index] = place.copy(isFavorite = !place.isFavorite)
            _places.value = currentList
        }
    }

    companion object {
        private val mockPlaces = listOf(
            Place(
                id = "1",
                name = "The Belverdere Grill",
                description = "The Belvedere Grill is a lively restaurant and chill spot with a strong cocktail game, plus views over Mombasa Road from the 6th floor. It’s open daily from 11 AM to 3 AM, so it works for both dinner and late-night hangs. Reviews are mixed on packages and crowd handling, but the service, ambience, and platters get plenty of love.",
                imageUrl = "https://img4.boatcdn.com/review_img/a3c9c6ef2e6a4a606e97e08cd54cc943",
                location = "6th Floor, NextGen Mall along Msa rd",
                rating = 7.5,
                reviewsCount = 1200,
                category = "Rooftop",
                priceRange = "$$$",
                distance = "2.5 km",
                tags = listOf("Chill Vibes", "Live DJ", "Craft Cocktails"),
                openingHours = listOf(
                    OpeningHour("Mon - Thu", "12:00 PM - 11:00 PM"),
                    OpeningHour("Fri - Sat", "12:00 PM - 02:00 AM", isToday = true),
                    OpeningHour("Sunday", "11:00 AM - 10:00 PM")
                )
            ),
            Place(
                id = "2",
                name = "Cultiva Farm",
                description = "Farm-to-table dining experience with a focus on fresh, local ingredients.",
                imageUrl = "https://images.unsplash.com/photo-1505275350441-83dcda8eeef5",
                location = "Pura Vida Farm, Karen",
                rating = 4.6,
                reviewsCount = 224,
                category = "Food",
                priceRange = "$$-$$$",
                distance = "8.2 km",
                tags = listOf("Eco-friendly", "Organic", "Outdoor"),
                waitTime = "15 - 20 mins"
            ),
            Place(
                id = "3",
                name = "The Copper Ivy ",
                description = "All Day Dining, Bar and Restaurant, Coffee Shop, Fine Dining, Take Away, Bakery, Delivery",
                imageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/2b/aa/8e/8f/caption.jpg?w=1100&h=600&s=1",
                location = "General Mathenge Drive The Promenade, Nairobi Kenya",
                rating = 3.8,
                reviewsCount = 36,
                category = "Chill",
                priceRange = "$$$",
                distance = "4.0 km"
            ),
            Place(
                id = "3",
                name = "The Alchemist Bar",
                description = "The Alchemist Bar is a collective of local and global entrepreneurs in food, fashion, music, and art. We are an outdoor venue in the heart of Westlands, Nairobi and host a wide variety of events on a nightly basis.",
                imageUrl = "https://images.squarespace-cdn.com/content/v1/63721d83ab8c8b1d0301dca8/1773757515532-AQY537VHM5E8RQ0V7AG5/image-asset.jpeg",
                location = "Parklands Rd, Westlands",
                rating = 3.9,
                reviewsCount = 84,
                category = "Chill",
                priceRange = "$$",
                distance = "1.8 km",
                tags = listOf("Nightlife", "Live Music", "Street Food")
            ),

            Place(
                id = "4",
                name = "Karura Forest",
                description = "The Karura Forest Reserve is an urban upland forest on the outskirts of Nairobi, the capital of Kenya. The forest offers eco-friendly opportunities for Kenyans and visitors to enjoy a leafy green respite from the hustle and bustle of the city to walk, to jog, or simply to sit quietly and experience the serenity of nature in all its diversity.",
                imageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/32/94/66/6c/caption.jpg?w=800&h=400&s=1",
                location = "Nairobi, Kenya",
                rating = 4.5,
                reviewsCount = 128,
                category = "Nature",
                priceRange = "$$",
                distance = "2.5 km",

                ),

            Place(
                id = "5",
                name = "Artcaffe Westlands",
                description = "A popular cafe and restaurant known for its stylish interiors, great coffee, and diverse menu. Perfect for casual meetups or remote work sessions.",
                imageUrl = "https://pub-5bcc3edf34304d04b59dc91e1ad9d2fd.r2.dev/kenya.tortoisepath.com/uploads/2024/03/13075421/Artcaffe-Westage-Mall-Westlands-Nairobi-Kenya-TortoisePathcom-4-1024x1024.webp",
                location = "Westlands, Nairobi",
                rating = 4.3,
                reviewsCount = 540,
                category = "Cafe",
                priceRange = "$$",
                distance = "2.0 km",
                tags = listOf("Coffee", "Work Friendly", "Brunch")
            ),

            Place(
                id = "6",
                name = "K1 Klub House",
                description = "A vibrant nightlife spot offering live music, themed nights, and a relaxed outdoor setting. Great for weekend hangouts.",
                imageUrl = "https://www.upkenya.com/wp-content/uploads/2020/11/K1-Klub-1024x768.jpg",
                location = "Kileleshwa, Nairobi",
                rating = 4.0,
                reviewsCount = 310,
                category = "Nightlife",
                priceRange = "$$",
                distance = "3.5 km",
                tags = listOf("Live Music", "DJ", "Outdoor")
            ),

            Place(
                id = "7",
                name = "Giraffe Centre",
                description = "A unique wildlife experience where visitors can feed and interact with endangered Rothschild giraffes.",
                imageUrl = "https://annestkenyasafaris.com/wp-content/uploads/2025/04/the-giraffes-seem-to-1024x682.jpg.webp",
                location = "Lang'ata, Nairobi",
                rating = 4.7,
                reviewsCount = 890,
                category = "Nature",
                priceRange = "$$",
                distance = "10.0 km",
                tags = listOf("Wildlife", "Outdoor", "Family Friendly")
            ),

            Place(
                id = "8",
                name = "Java House CBD",
                description = "A well-known Kenyan coffee chain offering reliable meals, coffee, and a relaxed environment in the city center.",
                imageUrl = "https://galleria.co.ke/storage/2022/04/Java-Logo.png",
                location = "Kenyatta Avenue, Nairobi",
                rating = 4.1,
                reviewsCount = 670,
                category = "Cafe",
                priceRange = "$$",
                distance = "1.2 km",
                tags = listOf("Coffee", "Meetups", "Quick Bites")
            ),

            Place(
                id = "9",
                name = "Two Rivers Mall",
                description = "One of the largest malls in East Africa featuring shopping, restaurants, entertainment, and a Ferris wheel.",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPVMC5CTDc7tHZVfl7-P4OOcXGJGcnHI-wPg&s",
                location = "Limuru Road, Nairobi",
                rating = 4.5,
                reviewsCount = 1200,
                category = "Entertainment",
                priceRange = "$$-$$$",
                distance = "9.5 km",
                tags = listOf("Shopping", "Cinema", "Family Friendly")
            ),

            Place(
                id = "10",
                name = "Hero Restaurant",
                description = "A trendy Asian-fusion restaurant with a bold comic-book aesthetic, offering sushi, cocktails, and a unique dining vibe.",
                imageUrl = "https://i0.wp.com/wareontheglobe.com/wp-content/uploads/2024/03/IMG_6376.jpeg?resize=705%2C435&ssl=1",
                location = "Village Market, Gigiri",
                rating = 4.6,
                reviewsCount = 410,
                category = "Food",
                priceRange = "$$$",
                distance = "8.8 km",
                tags = listOf("Sushi", "Cocktails", "Trendy")
            )
        )
    }
}
