package com.lexmasterteam.eggcounterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.lexmasterteam.eggcounterapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var isTimerPaused:Boolean = false
    var isTimerStarted:Boolean = false
    var TotalTime:Int=0
    var TimeStart: Int = 0
    var TimeToPass: MutableLiveData<Int> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        TimeToPass.value=0


        TimeStart = (System.currentTimeMillis()/1000).toInt()
        binding.EggBT.setOnClickListener {
            if(!isTimerStarted){
                TimeStart= (System.currentTimeMillis()/1000).toInt()
                isTimerStarted = true
                isTimerPaused = false
            }
            else if (isTimerPaused){
                isTimerPaused = !isTimerPaused
                GlobalScope.launch {
                    UpdateTimer()
                }
            }
            else{
                isTimerPaused = !isTimerPaused
            }


        }
        val timeObserver2 = Observer<Int>{

            newTime ->
            binding.TimerTV.text = "${(newTime/60)}:${newTime -60*(newTime/60)}"
        }

        GlobalScope.launch {
            UpdateTimer()
        }
        TimeToPass.observe(this,timeObserver2)



    }
    fun UpdateTimer(){
        while(!isTimerPaused){
            TotalTime+=(System.currentTimeMillis()/1000).toInt()-TimeStart
            if ((TotalTime/1000000).toInt()<=300){
                TimeToPass.postValue((300-TotalTime/1000000).toInt())
            }
            else{
                isTimerPaused=true
                binding.TimerTV.text= "END"

            }

        }
    }
}