package flore.cristi.project.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GenericRepo {
    public <T> T getEntityByUid(String uid, DatabaseReference database, Class<T> classType) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Object> val = new AtomicReference<>(null);
        final AtomicReference<Exception> exceptionAtomicReference = new AtomicReference<>(null);

        database.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    if (messageSnapshot.getKey().equals(uid)) {
                        val.set(messageSnapshot.getValue(classType));
                        latch.countDown();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                exceptionAtomicReference.set(databaseError.toException());
                latch.countDown();
            }
        });
        latch.await(30, TimeUnit.SECONDS);
        if (exceptionAtomicReference.get() != null) {
            System.out.println(exceptionAtomicReference);
        }
        return (T) val.get();
    }

    public <T> List<T> getAllEntities(DatabaseReference database, Class<T> classType) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Object> val = new AtomicReference<>(null);
        final AtomicReference<Exception> exceptionAtomicReference = new AtomicReference<>(null);

        database.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<T> list = new ArrayList<>();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    System.out.println(dataSnapshot.getChildrenCount());
                    list.add(messageSnapshot.getValue(classType));
                }
                val.set(list);
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                exceptionAtomicReference.set(databaseError.toException());
                latch.countDown();
            }
        });
        latch.await(30, TimeUnit.SECONDS);
        if (exceptionAtomicReference.get() != null) {
            System.out.println(exceptionAtomicReference);
        }
        return (List<T>) val.get();
    }

}
