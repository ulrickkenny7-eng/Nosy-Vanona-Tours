package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TourItemEntity::class, BookingEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tourDao(): TourDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nosy_vanona_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.tourDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: TourDao) {
            val initialItems = listOf(
                // Excursions
                TourItemEntity(
                    title = "Nosy Iranja (L'Île aux Tortues)",
                    category = "excursion",
                    shortDescription = "Journée paradisiaque sur l'île aux tortues reliées par un banc de sable blanc.",
                    fullDescription = "Nosy Iranja est constituée de deux îles reliées par un magnifique banc de sable blanc immaculé de 1,2 km de long. Profitez d'une baignade dans des eaux turquoises cristallines, observez les tortues marines géantes, visitez le village de pêcheurs et le phare historique dessiné par Gustave Eiffel. Déjeuner aux fruits de mer frais inclus.",
                    durationOrType = "Journée complète (08h00 - 16h30)",
                    location = "Nosy Iranja",
                    rating = 4.9f,
                    imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
                    isFeatured = true
                ),
                TourItemEntity(
                    title = "Réserve Naturelle de Lokobe",
                    category = "excursion",
                    shortDescription = "Immersion dans la forêt primaire et rencontre avec les lémuriens macaque.",
                    fullDescription = "Partez en pirogue traditionnelle à balancier à travers la mangrove pour rejoindre la réserve naturelle intégrale de Lokobe. Accompagné d'un guide officiel, découvrez la faune endémique de Madagascar : lémuriens makis et salamakas, caméléons panthères, geckos et plantes médicinales.",
                    durationOrType = "Demi-journée (07h30 - 13h00)",
                    location = "Lokobe Reserve, Nosy Be",
                    rating = 4.8f,
                    isFeatured = true,
                    imageUrl = "https://images.unsplash.com/photo-1544735716-392fe2489ffa"
                ),
                TourItemEntity(
                    title = "Nosy Tanikely (Parc Marin)",
                    category = "excursion",
                    shortDescription = "Snorkeling d'exception dans l'aquarium naturel du parc marin protégé.",
                    fullDescription = "Nosy Tanikely est un véritable aquarium à ciel ouvert. Équipez-vous de masque et tuba pour nager au milieu des coraux multicolores, poissons clowns, tortues et raies. Balade jusqu'au vieux phare pour admirer un panorama exceptionnel à 360° sur l'archipel.",
                    durationOrType = "Journée (08h30 - 15h00)",
                    location = "Nosy Tanikely",
                    rating = 4.9f,
                    isFeatured = true,
                    imageUrl = "https://images.unsplash.com/photo-1544551763-46a013bb70d5"
                ),
                TourItemEntity(
                    title = "Croisière Catamaran Coucher de Soleil",
                    category = "excursion",
                    shortDescription = "Navigation magique dans la baie de Nosy Be au coucher du soleil.",
                    fullDescription = "Vivez un moment inoubliable à bord d'un catamaran spacieux voguant doucement sur les flots calmes de la baie de Nosy Be. Dégustez des cocktails tropicaux et des amuse-bouches locaux tout en admirant le soleil flamboyant se coucher sur l'océan Indien.",
                    durationOrType = "3 heures (16h00 - 19h00)",
                    location = "Baie de Nosy Be",
                    rating = 5.0f,
                    isFeatured = false,
                    imageUrl = "https://images.unsplash.com/photo-1500375592092-40eb2168fd21"
                ),
                TourItemEntity(
                    title = "Observation des Baleines à Bosse",
                    category = "excursion",
                    shortDescription = "Rencontre majestueuse avec les baleines (de Juillet à Novembre).",
                    fullDescription = "Chaque année entre juillet et novembre, les baleines à bosse quittent l'Antarctique pour venir se reproduire dans les eaux chaudes de Madagascar. Embarquez avec nos biologistes et marins expérimentés pour observer ces géants des mers et leurs baleineaux en toute sécurité.",
                    durationOrType = "Demi-journée (08h00 - 12h30)",
                    location = "Canal de Mozambique",
                    rating = 4.9f,
                    isFeatured = false,
                    imageUrl = "https://images.unsplash.com/photo-1568430462989-44163eb1752f"
                ),

                // Accommodations
                TourItemEntity(
                    title = "Nosy Varona - Île Privée Eco-Lodge",
                    category = "accommodation",
                    shortDescription = "Séjour exclusif sur une île privée paradisiaque avec chef personnel.",
                    fullDescription = "Vivez le rêve absolu du Robinson Crusoé moderne sur l'île privée de Nosy Varona. Bungalows luxueux et écologiques face au lagon turquoise, service hôtelier sur mesure, chef cuisinier privé préparant des poissons frais et langoustes chaque jour.",
                    durationOrType = "Ecolodge Privé (Pension Complète)",
                    location = "Nosy Varona",
                    rating = 5.0f,
                    isFeatured = true,
                    imageUrl = "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4"
                ),
                TourItemEntity(
                    title = "Belamandy Lodge Beachfront",
                    category = "accommodation",
                    shortDescription = "Bungalows de charme au bord d'une plage de sable fin à Ankify.",
                    fullDescription = "Idéalement situé face à Nosy Be, le Belamandy Lodge offre un cadre verdoyant et paisible. Chambres climatisées, terrasse privative les pieds dans l'eau, piscine à débordement et restaurant aux saveurs tropicales et malgaches.",
                    durationOrType = "Chambre Double / Petit-déjeuner",
                    location = "Ankify / Côte Malgache",
                    rating = 4.8f,
                    isFeatured = true,
                    imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945"
                ),
                TourItemEntity(
                    title = "Lokobe Rainforest Villa",
                    category = "accommodation",
                    shortDescription = "Villa intimiste nichée dans l'écrin luxuriant de la forêt de Lokobe.",
                    fullDescription = "Pour les amoureux de nature et de calme absolu. Cette villa éco-responsable s'intègre harmonieusement dans la canopée. Endroit privilégié pour observer les lémuriens directement depuis votre terrasse et profiter de couchers de soleil enchanteurs.",
                    durationOrType = "Villa 2-4 personnes",
                    location = "Lokobe, Nosy Be",
                    rating = 4.7f,
                    isFeatured = false,
                    imageUrl = "https://images.unsplash.com/photo-1582719508461-905c673771fd"
                ),
                TourItemEntity(
                    title = "Sakatia Lodge & Spa",
                    category = "accommodation",
                    shortDescription = "Lodge authentique sur l'île aux orchidées, réputé pour sa plongée.",
                    fullDescription = "Niché sur l'île tranquille de Sakatia, ce lodge propose des bungalows traditionnels en bois précieux. Centre de plongée PADI intégré, jardin tropical luxuriant, plage sauvage et cuisine raffinée aux épices de Madagascar.",
                    durationOrType = "Bungalow Tropical",
                    location = "Île de Sakatia",
                    rating = 4.8f,
                    isFeatured = false,
                    imageUrl = "https://images.unsplash.com/photo-1590490360182-c33d57733427"
                ),

                // Tourist Services
                TourItemEntity(
                    title = "Transfert Aéroport & Port Privé",
                    category = "service",
                    shortDescription = "Accueil VIP à l'aéroport de Nosy Be et transfert sécurisé.",
                    fullDescription = "Service de transfert complet et ponctuel. Accueil dès votre descente d'avion à l'aéroport de Nosy Be Fascene, transport en véhicule climatisé récent jusqu'au port d'embarquement, puis traversée en bateau rapide privé directement vers votre hôtel ou île.",
                    durationOrType = "Service VIP / Sur réservation",
                    location = "Aéroport Nosy Be Fascene",
                    rating = 4.9f,
                    isFeatured = true,
                    imageUrl = "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957"
                ),
                TourItemEntity(
                    title = "Location de Bateau Rapide Charter",
                    category = "service",
                    shortDescription = "Mise à disposition d'un speed-boat avec skipper pour vos excursions privées.",
                    fullDescription = "Louez un bateau rapide entièrement privatisé avec skipper et marin professionnels. Idéal pour explorer l'archipel de Nosy Be à votre rythme, visiter des criques secrètes, pêcher ou organiser un pique-nique sur une île déserte.",
                    durationOrType = "Journée complète avec carburant",
                    location = "Départ Port de Hell-Ville",
                    rating = 5.0f,
                    isFeatured = true,
                    imageUrl = "https://images.unsplash.com/photo-1569263979104-865ab9cd8d59"
                ),
                TourItemEntity(
                    title = "Guide Touristique Multilingue Expert",
                    category = "service",
                    shortDescription = "Accompagnement personnalisé par un guide officiel diplômé.",
                    fullDescription = "Nos guides locaux passionnés maîtrisent parfaitement le français, l'anglais et l'italien. Ils vous dévoileront tous les secrets de l'histoire, de la culture sakalava, de la faune et de la flore unique de Madagascar.",
                    durationOrType = "Journée complète (08h00 - 17h00)",
                    location = "Tout Madagascar / Nosy Be",
                    rating = 4.8f,
                    isFeatured = false,
                    imageUrl = "https://images.unsplash.com/photo-1516738901171-8eb4fc13bd20"
                ),
                TourItemEntity(
                    title = "Location Quad & 4x4 Tout-Terrain",
                    category = "service",
                    shortDescription = "Explorez les pistes de l'île aux parfums en toute liberté.",
                    fullDescription = "Partez à l'aventure sur les pistes rouges de Nosy Be au guidon de quads puissants ou à bord de véhicules 4x4 récents. Visitez le Mont Passot, les lacs sacrés aux crocodiles et les plantations d'ylang-ylang.",
                    durationOrType = "Location 24h",
                    location = "Hell-Ville, Nosy Be",
                    rating = 4.7f,
                    isFeatured = false,
                    imageUrl = "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf"
                )
            )
            dao.insertAll(initialItems)
        }
    }
}
