package com.pethealthcompanion.app.database;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.pethealthcompanion.app.dao.AppointmentDAO;
import com.pethealthcompanion.app.dao.PetDAO;
import com.pethealthcompanion.app.dao.UserDAO;
import com.pethealthcompanion.app.entities.Appointment;
import com.pethealthcompanion.app.entities.Pet;
import com.pethealthcompanion.app.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private AppointmentDAO mAppointmentDAO;
    private PetDAO mPetDAO;
    private UserDAO mUserDao;
    private List<Appointment> mAllAppointments;
    private List<Pet> mAllPets;

    private LiveData<List<Pet>> searchResults;

    public Repository() {

    }

    public interface Callback<T> {
        void onResult(T result);
        void onError(Exception e);
        void onAppointmentLoaded(Appointment appointment);
    }
    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private ExecutorService executorService;


    public Repository(Application application) {
        AppointmentDatabaseBuilder db = AppointmentDatabaseBuilder.getDatabase(application);
        mPetDAO = db.petDAO();
        mAppointmentDAO = db.appointmentDAO();
        mUserDao = db.userDAO();
    }

    public Repository(PetDAO petDAO) {
        this.mPetDAO = petDAO;
    }

    public Repository(UserDAO userDAO) {
        this.mUserDao = userDAO;
    }

    public Repository(AppointmentDAO appointmentDAO) {this.mAppointmentDAO = appointmentDAO;}


    public void insertUser(User user) {
        new InsertUserAsyncTask(mUserDao).execute(user);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO mUserDao;

        InsertUserAsyncTask(UserDAO userDao) {
            this.mUserDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            mUserDao.insert(users[0]);
            return null;
        }
    }


    public LiveData<User> getUserByUsername(String username) {
        if (mUserDao != null) {
            return mUserDao.getUserByUsernameLiveData(username);
        } else {
            Log.e("Repository", "UserDAO is null. Unable to fetch user by username.");
            MutableLiveData<User> nullData = new MutableLiveData<>();
            nullData.setValue(null);
            return nullData;
        }
    }

    public void getAllAppointmentsAsync(Callback<List<Appointment>> callback) {
        databaseExecutor.execute(() -> {
            List<Appointment> appointments = mAppointmentDAO.getAllAppointments();
            callback.onResult(appointments);
        });
    }


    public List<Appointment> getAllAppointments() {
        databaseExecutor.execute(() -> {
            mAllAppointments = mAppointmentDAO.getAllAppointments();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllAppointments;
    }

    public Appointment getAppointmentById(int appointmentID) {
        for (Appointment appointment : getAllAppointments()) {
            if (appointment.getAppointmentID() == appointmentID) {
                return appointment;
            }
        }
        return null;
    }


    public Appointment findAppointmentById(List<Appointment> appointments, int appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() == appointmentId) {
                return appointment;
            }
        }
        return null;
    }

    public void insert(Appointment appointment){
        databaseExecutor.execute(()->{
            mAppointmentDAO.insert(appointment);
        });
    }

    public void update(Appointment appointment){
        databaseExecutor.execute(()->{
            mAppointmentDAO.update(appointment);
        });
    }

    public void delete(Appointment appointment){
        databaseExecutor.execute(()->{
            mAppointmentDAO.delete(appointment);
        });
    }

    public LiveData<List<Pet>> getAllPets() {
        return mPetDAO.getAllPets();
    }

    public Pet getPetById(int petID) {
        return mPetDAO.getPetById(petID);
    }

    public void getMaxPetID(OnMaxPetIDCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int maxPetID = mPetDAO.getLastPetID();
                    callback.onMaxPetIDReceived(maxPetID);
                } catch (Exception e) {
                    Log.e(TAG, "Error getting max pet ID", e);
                    callback.onMaxPetIDReceived(0);
                }
            }
        }).start();
    }

    public interface OnMaxPetIDCallback {
        void onMaxPetIDReceived(int maxPetID);
    }



    public LiveData<List<Pet>> searchPetsByName(String searchQuery) {
        LiveData<List<Pet>> petSearchResults;
        LiveData<List<Appointment>> appointmentSearchResults;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            petSearchResults = mPetDAO.searchPetsByName(searchQuery.trim());
            appointmentSearchResults = mAppointmentDAO.searchPetsByName(searchQuery.trim());
        } else {
            petSearchResults = new MutableLiveData<>();
            appointmentSearchResults = new MutableLiveData<>();
        }

        final MediatorLiveData<List<Pet>> combinedSearchResults = new MediatorLiveData<>();

        final MutableLiveData<List<Pet>> uniquePets = new MutableLiveData<>();

        combinedSearchResults.addSource(petSearchResults, pets -> {
            List<Pet> uniqueList = new ArrayList<>(pets);
            uniquePets.setValue(uniqueList);
        });

        combinedSearchResults.addSource(appointmentSearchResults, appointments -> {
            List<Pet> appointmentPets = new ArrayList<>();
            for (Appointment appointment : appointments) {
                appointmentPets.add(new Pet(
                        appointment.getAppointmentID(),
                        appointment.getPetName(),
                        appointment.getAppointmentDate(),
                        appointment.getVeterinaryClinic(),
                        appointment.getNotes()
                ));
            }

            List<Pet> finalUniqueList = null;
            if (uniquePets.getValue() != null) {
                finalUniqueList = new ArrayList<>(uniquePets.getValue());
            } else {
                finalUniqueList = new ArrayList<>();
            }


            for (Pet appointmentPet : appointmentPets) {
                if (!finalUniqueList.contains(appointmentPet)) {
                    finalUniqueList.add(appointmentPet);
                }
            }

            combinedSearchResults.setValue(finalUniqueList);
        });

        return combinedSearchResults;
    }



    public LiveData<List<Pet>> observeSearchResults() {
        return searchResults;
    }

    public LiveData<List<String>> getAllPetNames() {
        return mPetDAO.getAllPetNames();
    }


    public boolean addNewAppointmentRecord(Appointment appointment) {
        try {
            mAppointmentDAO.insert(appointment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAppointmentRecord(Appointment appointment) {
        try {
            mAppointmentDAO.delete(appointment);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean addNewPetRecord(Pet pet) {
        try {
            mPetDAO.insert(pet);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePetRecordByName(String petName) {
        try {
            mPetDAO.delete(petName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insert(Pet pet){
        databaseExecutor.execute(()->{
            mPetDAO.insert(pet);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Pet pet){
        databaseExecutor.execute(()->{
            mPetDAO.update(pet);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(Pet pet){
        databaseExecutor.execute(()->{
            mPetDAO.delete(pet);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
