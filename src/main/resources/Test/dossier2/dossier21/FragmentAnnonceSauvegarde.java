package Test.dossier2.dossier21;

import Test.dossier1.dossier12.DetailAnnonce;
import Test.dossier2.AnnonceRecentAdaptateur;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import com.example.projetmobile.BDD.Repository.AppDataBase;
import com.example.projetmobile.BDD.Repository.UserDao;
import com.example.projetmobile.BDD.models.Controllers.UserControlers;
import com.example.projetmobile.Model.Annonce;
import com.example.projetmobile.Model.serveur;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class FragmentAnnonceSauvegarde extends Fragment {
    private GridView listView ;
    private ArrayList<Annonce> annonces;
    AnnonceRecentAdaptateur myAdapter;

    public FragmentAnnonceSauvegarde() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_annonce_sauvegarde, container, false);
        listView = (GridView ) root.findViewById(R.id.gridView);
        Gson gson = new Gson();
        AppDataBase db = Room.databaseBuilder(getContext(), AppDataBase.class,"database-name").allowMainThreadQueries().build();
        UserDao userDao = db.userDao();
        String reponse = null;
        try {
            serveur s = new serveur("annonce/Getsauvegarde/" + userDao.getAll().get(0).getId_user());
            reponse = s.getRequest();
            // reponse = myAsyncTasks.execute(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!reponse.equals("")) {
            annonces = gson.fromJson(reponse,  new TypeToken<ArrayList<Annonce>>(){}.getType());
            myAdapter=new AnnonceRecentAdaptateur(getContext(),annonces);
            listView.setAdapter(myAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                final int REQUEST_CODE = 20;
                Intent intention= new Intent(getContext(), DetailAnnonce.class);
                Gson gson = new Gson();
                FileOutputStream fOut = null;
                String myJson = gson.toJson(annonces.get(position));
                intention.putExtra("Annonce",myJson);
                intention.putExtra("FAV",myAdapter.getFav().get(position));
                startActivity(intention);
            }
        });


        return root;
    }


}