package volley;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
	private static String base_URL = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/";		//Default configuration for WAMP - 80 is default port for WAMP and 10.0.2.2 is localhost IP in Android Emulator
	// Server user login url
	public static String URL_LOGIN = base_URL+"Biodiversitywariors/";

	// Server user register url
	public static String URL_REGISTER = base_URL+"Biodiversitywariors/";

	//method get
	public static String VIEW_ALLJURNAL = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/read.php";
	public static String VIEW_JURNAL = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/viewjurnal.php";
	public static String VIEW_ARTIKEL = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/viewartikel.php";
	public static String DETAIL_POSTING = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/get_data.php";
	public static String VIEW_KATALOG = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/viewkatalog.php";
	public static String VIEW_GALERI = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/viewgaleri.php";
	public static String VIEW_PERINGKAT = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/viewperingkat.php";
	public static String VIEW_DETAIL_PROFIL_USER = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/viewdetailprofiluser.php";
	public static String VIEW_MY_JURNAL = "http://biodiversitywarriors.lskk.ee.itb.ac.id/V1Android/Biodiversitywariors/bw/myjurnal.php";

	//insert jurnal
	//public static String UPLOAD_JURNAL ="http://biodiversitywarriors.lskk.ee.itb.ac.id/gambar/artikel/thumb/uploadimage.php";
	//public static final String TAG_SUCCESS = "success";
	//public static final String TAG_MESSAGE = "message";
	//public static String tag_json_obj = "json_obj_req";
/*
	private static final String URL_JSON_OBJECT = "http://api.androidhive.info/volley/person_object.json";
	private static final String URL_JSON_ARRAY = "http://api.androidhive.info/volley/person_array.json";
	private static final String URL_STRING_REQ = "http://api.androidhive.info/volley/string_response.html";
	private static final String URL_IMAGE = "http://api.androidhive.info/volley/volley-image.jpg";

	//If you need any parameter passed with the URL (GET) - U need to modify this functions
	public static String get_JSON_Object_URL()
	{
		return URL_JSON_OBJECT;
	}



	public static String get_JSON_Array_URL()
	{
		return URL_JSON_ARRAY;
	}

	public static String get_String_URL(String Input)
	{
		if(Input.length()>0) {
			return Input;
		}
		return URL_STRING_REQ;
	}

	public static String get_Image_URL()
	{
		return URL_IMAGE;
	}
	*/
}
