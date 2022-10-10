package com.example.covidrun.ui.faq;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covidrun.R;
import com.example.covidrun.faq.FaqAdapter;
import com.example.covidrun.model.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class FaqFragment extends Fragment {
    RecyclerView recyclerView;
    List<FAQModel> FaqList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate((R.layout.fragment_faq), container, false);
        recyclerView = view.findViewById(R.id.FAQRecyclerView);

        initData();
        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        FaqAdapter faqAdapter = new FaqAdapter(FaqList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(faqAdapter);
    }

    private void initData(){
        FaqList = new ArrayList<>();
        FaqList.add(new FAQModel("What is coronavirus","Coronavirus (CoV) is a virus that can cause respiratory tract infections. There are several types of coronavirus such as severe acute respiratory syndrome coronavirus (SARS) and Middle East Respiratory syndrome-related coronavirus (MER-CoV). The latest Coronavirus found in China is Novel Coronavirus 2019 (COVID-19)."));
        FaqList.add(new FAQModel("How novel coronavirus 2019 (COVID-19) spread?", "Mode of transmission of novel coronavirus COVID-19 from individuals is via droplets from the nose or mouth when someone with COVID- 19 coughed or sneezed. It spreads across objects and surfaces around it. Other people get COVID-19 infection by touching this object or surface, then touching their eyes, nose or mouth. A person may also be infected with COVID-19 if they are in close contact with a COVID-19 patient who is coughing or sneezing or releasing a droplets. That's why it's important to maintain a distance of more than 1 meter (3 feet) from a sick person.\n" +
                "\n" +
                "MOH is currently monitoring the progress of this infection and the situation is changing and will update the disease information on the MOH website from time to time. People are advised to be vigilant and to maintain good personal hygiene."));
        FaqList.add(new FAQModel("Symptoms", "Symptoms of COVID-19 are fever, cough and breathing difficulty."));
        FaqList.add(new FAQModel("What is the treatment for COVID-19 infection?", "To date, there have been no specific treatments or antiviral for COVID-19 infection. Treatment is given only to relieve the symptoms of the patient."));
        FaqList.add(new FAQModel("Prevention",
                "Travelers to countries with cases of COVID-19 and after returning to Malaysia is cautioned to always practice the following steps:\n" +
                        "Practice high levels of personal hygiene at all times such as washing your hands frequently with water and soap or hand sanitizer;\n" +
                        "Always bring face mask as well as hand sanitizer for use when needed during the visit;\n" +
                        "Avoid visiting crowded area or having close contact with any symptomatic individuals during the visit;\n" +
                        "Avoid the visiting animals farm, markets selling live animals, slaughter of animals or touching any animals during the period of the visit;\n" +
                        "Avoid eating any raw animal products during travel period."));
        FaqList.add(new FAQModel("COVID-19 Hotline Number", "+60 3 8881 0200"));
    }

}