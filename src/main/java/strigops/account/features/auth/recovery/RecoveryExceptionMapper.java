//package strigops.account.features.security.recovery;
//
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.ext.ExceptionMapper;
//import jakarta.ws.rs.ext.Provider;
//import java.util.Map;
//
//@Provider
//public class RecoveryExceptionMapper implements ExceptionMapper<Exception> {
//
//    @Override
//    public Response toResponse(Exception exception){
//        if (exception instanceof IllegalArgumentException || exception instanceof IllegalStateException){
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .type(MediaType.APPLICATION_JSON)
//                    .entity(Map.of("error", exception.getMessage()))
//                    .build();
//        }
//
//        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                .type(MediaType.APPLICATION_JSON)
//                .entity(Map.of("error", "An internal error occurred on the server"))
//                .build();
//    }
//}
