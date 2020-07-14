package vztrack.gls.com.vztrack_user.utils;

/**
 * Created by sandeep on 3/3/16.
 */
public class URL {
    public static final String LOGIN = "/api/userLogin";
    public static final String NOTICES = "/api/notices";
    public static final String NOTICES_UPDATED = "/api/sendNoticesUpdated";
    public static final String DOWNLOAD_NOTICE_FILE = "/api/downloadNoticeFile";
    public static final String SEND_NOTICE = "/api/sendNotices";
    public static final String VISITORS = "/api/visitors?pageNo=";
    public static final String VISIT_LIST = "/api/visitorDetails?mobileNo=";
    public static final String CHANGE_PASSWORD = "/api/changePassword?newPassword=";
    public static final String LOGOUT = "/api/logout";

    // SOS API
    public static final String SOS = "/api/sos";                                                    // GET Method
    public static final String FEEDBACK = "/api/feedback";                                            // POST Method

    // Rating API
    public static final String SAVE_RATINGS = "/saveRatings";
    public static final String PENDING_RATINGS = "/getPendingRatings";
    public static final String CANCLE_RATING = "/cancelRating";
    public static final String PROVIDER_LIST = "/getProviderList";
    public static final String PROVIDERS_DATA = "/getProviderData";
    public static final String PROVIDER_DETAIL_DATA = "/getProviderDetailData";

    // Complain API
    public static final String ADD_COMPLAINS = "/api/addSocietyComplain";
    public static final String GET_COMPLAINS = "/api/getSocietyComplains";      // Get Query Parameter
    public static final String CLOSE_COMPLAINS = "/api/closeSocietyComplain";
    public static final String GET_COMPLAIN_CATEGORY = "/api/getSocietyComplainCategory";

    public static final String GET_COMPLAINT_DETAILS = "/api/getComplainDetails";
    public static final String SAVE_COMPLAINT_COMMENT = "/api/saveNewComplainComment";
    public static final String CHANGE_COMPLAINT_STATUS = "/api/changeComplainStatus";

    // Incorrect visitor mark API
    public static final String MARK_ERROR = "/api/markError?id=";
    public static final String VEHICLE_NO_PATTERN = "/api/vehicleNoPattern";
    public static final String SEARCH_VEHICLE = "/api/getSearchFlatVehicle?vehicleNumber=";
    public static final String ADD_VEHICLE = "/api/addFlatVehicle";                                 // GET METHOD
    public static final String GET_FLAT_VEHICLES = "/api/getFlatVehicles";                          // GET METHOD
    public static final String DELETE_FLAT_VEHICLES = "/api/deleteFlatVehicle";                     // GET METHOD
    public static final String MESSAGE = "/api/getFlatMessages?pageNo=";

    // Message API
    public static final String GET_ALL_FLATS = "/api/getAvailableFlatsByLogin";
    public static final String GET_ALL_GROUPS = "/api/getAllGroups";
    public static final String SEND_MESSAGE = "/api/addNewMessage";
    public static final String SEND_MESSAGE_NOTIFICATION = "/api/sendMessageNotification";

    public static final String SEND_VEHICLE_MESSAGE_NOTIFICATION = "/api/sendVehicleNotification";

    // Local Store API
    public static final String LOCAL_STORES = "/api/getMyLocalStores";
    public static final String STORE_CALL_LOG = "/api/loggStoreCallsHistory";

    // Polls API
    public static final String POLL_OPERATIONS = "/api/pollOperations";
    public static final String GET_USER_POLLS = "/api/getAllUserPolls";
    public static final String GET_ADMIN_POLLS = "/api/getAllAdminPolls";
    public static final String SAVE_USER_POLL = "/api/saveUserPoll";

    // Link Email id to User Account
    public static final String ADD_EMAIL = "/api/linkEmail";

    // Domestic help API
    public static final String DOMESTIC_HELP = "/api/domesticHelp?purpose=";

    // Car Pool
    public static final String CAR_POOL_ADD_REQUEST = "/api/addCarPoolRequest";
    public static final String CAR_POOL_ADD_OFFER = "/api/addCarPoolOffer";
    public static final String GET_CAR_POOL_REQUEST = "/api//getAllCarPoolRequests";
    public static final String GET_CAR_POOL_OFFERS = "/api/getAllCarPoolOffers";

    public static final String CLOSE_CAR_POOL_REQUEST = "/api/closeCarPoolRequest?carPoolId=";
    public static final String CLOSE_CAR_POOL_OFFERS = "/api/closeCarPoolOffer?carPoolId=";

    // market Place API
    public static final String CREATE_EDIT_CLOSE_ADV = "/api/advertiseAction";
    public static final String GET_ALL_ADV = "/api/getAllMarketplaceAd";
    public static final String GET_DETAILS_OF_ADV = "/api/getMarketplaceAdDetails";   // Query Parameter
    public static final String SAVE_NEW_MARKET_PLACE_COMMENT = "/api/saveNewMarketplaceComment";

    //invitation urls
    public static final String INVITATION_PURPOSE = "/api/getInviteGuestPurposes";
    public static final String INVITATION_LIST = "/api/getInvitationsList";
    public static final String SAVE_INVITATION = "/api/saveNewInvitation";
    public static final String INVITATION_DETAIL = "/api/getInvitationDetails";

    // Rainbow API
    public static final String RAINBOW_USERS = "/api/getNeighbourRainbowUsers?userLoggedInRainbowId=";
    public static final String RAINBOW_ACCOUNT_DETAILS = "/api/getRainbowAccountDetails";

    // Swipez Invoice
    public static final String INVOICE_LIST = "/api/getInvoiceList";
    public static final String GET_INVOICE_URL = "/api/getInvoiceUrl?invoice_id=";
    public static final String DUE_INVOICE_INFO = "/api/dueInvoicesInfo";

    public static final String SENT_MESSAGE_API = "/api/getAllSentMessagesOfSociety";
    public static final String SENT_MESSAGE_DETAIL = "/api/getSentMessageDetails";

    // Secondary User API
    public static  final String GET_SECONDARY_USER = "/api/getSecondoryUser";
    public static final String DELETE_SECONDARY_USER =  "/api/deleteSecondoryUser?loginId=";
    public static final String ADD_SECONDARY_USER = "/api/createSecondoryUser";

    //Notification Menu
    public static final String GET_NOTIFICATION_MENU = "/api/getNotificationOptionList";
    public static final String SAVE_NOTIFICATION_OPTION = "/api/saveNotificationOptions";

    //Pre approval api

    public static final String ADD_PREAPPROVAL="/api/addPreApproval";

    //get pre approval purpose list api

    public static final String GETPREAPPROVALPURPOSE="/api/getPreApprovalPurpose";

    //get approval list

    public static final String GETPREAPPROVALIST="/api/getPreApprovalList";
    //DELETE PRE approval

    public static final String DELETEPREAPPROVAL="/api/deletePreApproval?preApprovalId=";



//     save photo of user url

    public static final String SAVE_USER_PHOTO="/api/saveUserPhoto";

    public static final String UPDATE_VISITOR_STATUS = "/api/updateVisitorStatus";

    //reopen complaint
    public static final String REOPENCOMPLAINT="/reopenComplaint";


    public static final String EXTRA_FIELD_ONE="/api/extraActivity";
    public static final String EXTRA_FIELD_TWO="/api/extraActivityOfCovid";

}
