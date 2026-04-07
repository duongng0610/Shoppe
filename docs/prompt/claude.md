viết lại các controller , service, fix lại repository có liên quan đến chat , 
-1:api : /user/conversations trả về List<ConversationLineProjection> , service dùng authentication.getPrinciple() để lấy userId , repo thì dùng native query , trar ve @src/main/java/com/e_cormerce/shoppe/projection/ConversationLineProjection.java  
-2:api :/targetId : thì trả về ConversationLineDto : 