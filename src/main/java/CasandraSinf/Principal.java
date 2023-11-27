package CasandraSinf;



public class Principal {

	public static void main(String[] args) {
		
		_Mongo _mongo = new _Mongo();
		
		/*
		 * Conexión a la base de datos Mongo.
		 */
		
		System.out.println("Conectando al cliente de la base de datos NoSQL MongoDB...");
		_mongo.connect();
		
		/*
		 * Creación de la base de datos ejemplo
		 */
		
		System.out.println("Creación de la base de datos ...");
        _mongo.Connect_Collection("ejemplo");
        
        /*
         * Eliminación de la colección si exsite.
         */
        
        _mongo.eliminar_coleccion("ejemplo");
        
        /*
         * Creación de la colección.
         */
        System.out.println("Creación de la colección ...");
        _mongo.eliminar_coleccion("restaurants");
        
        
        /*
         * Insertamos un elemento en la colección restaurantes
         * La colección no existe, pero al igual que la creación de la base de datos si no existe la colección la crea.
         */
        
        System.out.println("Insertamos un elemento en la colección restaurantes");
        _mongo.insertOneDataTest();
        
        System.out.println("Listamos los elementos de la colección");
        _mongo.listRestaurants();
        
        System.out.println("Insertamos un conjunto de elementos en la colección restaurantes");
        _mongo.insertManyDataTest();
        
        System.out.println("Listamos los elementos de la colección");
        _mongo.listRestaurants();
        
        System.out.println("Listamos un elemento determinado de la colección");
        _mongo.listRestaurantsByCuisine("Ourense");

	}

}
