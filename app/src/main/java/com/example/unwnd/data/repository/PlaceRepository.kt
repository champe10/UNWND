package com.example.unwnd.data.repository

import com.example.unwnd.data.local.dao.PlaceDao
import com.example.unwnd.data.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PlaceRepository(private val placeDao: PlaceDao) {

    val places: Flow<List<Place>> = placeDao.getAllPlaces()

    suspend fun initializeData() {
        val currentPlaces = placeDao.getAllPlaces().first()
        if (currentPlaces.isEmpty()) {
            placeDao.insertPlaces(mockPlaces)
        }
    }

    fun getPlaceById(id: String): Flow<Place?> = placeDao.getPlaceById(id)

    suspend fun toggleFavorite(id: String) {
        val place = placeDao.getPlaceById(id).first()
        place?.let {
            placeDao.updateFavoriteStatus(id, !it.isFavorite)
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
                longitude = -1.3237924441667397,
                latitude = .844254976724116,
                priceRange = "$$$",
                tags = listOf("Chill Vibes", "Live DJ", "Craft Cocktails"),

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
                tags = listOf("Eco-friendly", "Organic", "Outdoor"),

                longitude = -1.3568239430628966,
                latitude = 36.73935839206449

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
                tags = listOf("Coffee", "Bar", "Fine Dining"),
                longitude = -1.2805409399587722,
                latitude = 36.7684698804247
            ),
            Place(
                id = "4",
                name = "The Alchemist Bar",
                description = "The Alchemist Bar is a collective of local and global entrepreneurs in food, fashion, music, and art. We are an outdoor venue in the heart of Westlands, Nairobi and host a wide variety of events on a nightly basis.",
                imageUrl = "https://images.squarespace-cdn.com/content/v1/63721d83ab8c8b1d0301dca8/1773757515532-AQY537VHM5E8RQ0V7AG5/image-asset.jpeg",
                location = "Parklands Rd, Westlands",
                rating = 3.9,
                reviewsCount = 84,
                category = "Chill",
                priceRange = "$$",
                tags = listOf("Nightlife", "Live Music", "Street Food"),
                longitude = -1.2623954171814267,
                latitude = .804104361376325
            ),
            Place(
                id = "5",
                name = "Karura Forest",
                description = "The Karura Forest Reserve is an urban upland forest on the outskirts of Nairobi, the capital of Kenya. The forest offers eco-friendly opportunities for Kenyans and visitors to enjoy a leafy green respite from the hustle and bustle of the city to walk, to jog, or simply to sit quietly and experience the serenity of nature in all its diversity.",
                imageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/32/94/66/6c/caption.jpg?w=800&h=400&s=1",
                location = "Nairobi, Kenya",
                rating = 4.5,
                reviewsCount = 128,
                category = "Nature",
                priceRange = "$$",
                tags = listOf("Forest", "Outdoor", "Eco-Friendly"),
                longitude = -1.235072320067873,
                latitude = 36.836814880284344
                ),
            Place(
                id = "6",
                name = "Artcaffe Westlands",
                description = "A popular cafe and restaurant known for its stylish interiors, great coffee, and diverse menu. Perfect for casual meetups or remote work sessions.",
                imageUrl = "https://pub-5bcc3edf34304d04b59dc91e1ad9d2fd.r2.dev/kenya.tortoisepath.com/uploads/2024/03/13075421/Artcaffe-Westage-Mall-Westlands-Nairobi-Kenya-TortoisePathcom-4-1024x1024.webp",
                location = "Westlands, Nairobi",
                rating = 4.3,
                reviewsCount = 540,
                category = "Cafe",
                priceRange = "$$",
                tags = listOf("Coffee", "Work Friendly", "Brunch"),
                longitude = -1.2630454095883359,
                latitude = 36.80195155716095
            ),
            Place(
                id = "7",
                name = "K1 Klub House",
                description = "A vibrant nightlife spot offering live music, themed nights, and a relaxed outdoor setting. Great for weekend hangouts.",
                imageUrl = "https://www.upkenya.com/wp-content/uploads/2020/11/K1-Klub-1024x768.jpg",
                location = "Kileleshwa, Nairobi",
                rating = 4.0,
                reviewsCount = 310,
                category = "Nightlife",
                priceRange = "$$",
                tags = listOf("Live Music", "DJ", "Outdoor"),
                longitude = -1.2680136127337607,
                latitude = 36.81220400555643
            ),
            Place(
                id = "8",
                name = "Giraffe Centre",
                description = "A unique wildlife experience where visitors can feed and interact with endangered Rothschild giraffes.",
                imageUrl = "https://annestkenyasafaris.com/wp-content/uploads/2025/04/the-giraffes-seem-to-1024x682.jpg.webp",
                location = "Lang'ata, Nairobi",
                rating = 4.7,
                reviewsCount = 890,
                category = "Nature",
                priceRange = "$$",
                tags = listOf("Wildlife", "Outdoor", "Family Friendly"),
                longitude = -1.3761976510178946,
                latitude = 36.74507864735747
            ),
            Place(
                id = "9",
                name = "Java House CBD",
                description = "A well-known Kenyan coffee chain offering reliable meals, coffee, and a relaxed environment in the city center.",
                imageUrl = "https://galleria.co.ke/storage/2022/04/Java-Logo.png",
                location = "Kenyatta Avenue, Nairobi",
                rating = 4.1,
                reviewsCount = 670,
                category = "Cafe",
                priceRange = "$$",
                tags = listOf("Coffee", "Meetups", "Quick Bites"),
                longitude = -1.2750919259421247,
                latitude = 36.82294439075688
            ),
            Place(
                id = "10",
                name = "Two Rivers Mall",
                description = "One of the largest malls in East Africa featuring shopping, restaurants, entertainment, and a Ferris wheel.",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPVMC5CTDc7tHZVfl7-P4OOcXGJGcnHI-wPg&s",
                location = "Limuru Road, Nairobi",
                rating = 4.5,
                reviewsCount = 1200,
                category = "Entertainment",
                priceRange = "$$-$$$",
                tags = listOf("Shopping", "Cinema", "Family Friendly"),
                longitude = -1.2115791816097337,
                latitude = 36.796373845505315
            ),
            Place(
                id = "11",
                name = "Hero Restaurant",
                description = "A trendy Asian-fusion restaurant with a bold comic-book aesthetic, offering sushi, cocktails, and a unique dining vibe.",
                imageUrl = "https://i0.wp.com/wareontheglobe.com/wp-content/uploads/2024/03/IMG_6376.jpeg?resize=705%2C435&ssl=1",
                location = "Village Market, Gigiri",
                rating = 4.6,
                reviewsCount = 410,
                category = "Food",
                priceRange = "$$$",
                tags = listOf("Sushi", "Cocktails", "Trendy"),
                longitude = -1.2303898832236093,
                latitude = 36.804825760849404
            )
        )
    }
}
