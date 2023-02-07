package it.unipi.lsmsd.nysleep;

import it.unipi.lsmsd.nysleep.DAO.mongoDB.MongoAccommodationDAO;
import it.unipi.lsmsd.nysleep.DAO.neo4jDB.NeoAccommodationDAO;
import it.unipi.lsmsd.nysleep.RMI.ServerRegistry;
import it.unipi.lsmsd.nysleep.business.exception.BusinessException;
import it.unipi.lsmsd.nysleep.model.Accommodation;
import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ServerStarter {

    private static void updateRoutine() throws BusinessException {
            NeoAccommodationDAO neoAccDAO = new NeoAccommodationDAO();
            MongoAccommodationDAO mongoAccDAO = new MongoAccommodationDAO();
            LinkedList<Document> docs = (LinkedList<Document>) mongoAccDAO.getAccHomePage(0, 0);
            double rating;
            mongoAccDAO.startTransaction();
            try{
                for(Document doc: docs){
                    Accommodation acc = new Accommodation();
                    acc.setId(doc.getInteger("_id"));
                    rating = neoAccDAO.recomputeRate(acc);
                    mongoAccDAO.updateRating(acc, rating);
                    neoAccDAO.updateRating(acc, rating);
                    mongoAccDAO.cleanReservations(acc);
                }
                mongoAccDAO.commitTransaction();
            }catch (Exception e){
                mongoAccDAO.abortTransaction();
                throw new BusinessException(e);
            }
            finally {
                mongoAccDAO.closeConnection();
            }

    }

    public static void main(String[] args) throws ParseException {

        ServerRegistry.exposeObjects();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = String.valueOf(LocalDate.now());
        now += " 23:59:59";
        Date date = dateFormatter.parse(now);
        Timer timer = new Timer();

        TimerTask updater = new TimerTask() {
            @Override
            public void run() {
                try {
                    ServerStarter.updateRoutine();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        timer.schedule(updater, date);


    }
}
