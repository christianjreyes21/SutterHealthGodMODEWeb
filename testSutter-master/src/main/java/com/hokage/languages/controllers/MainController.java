package com.hokage.languages.controllers;

import static org.mockito.Mockito.mockitoSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.websocket.Session;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hokage.languages.models.Hostname;

@Controller
public class MainController {
	@RequestMapping("/") 
	public String index(@ModelAttribute("hostname") Hostname hostname) {
		return "index.jsp";
	}
	
	@PostMapping("/test")
	public String test(HttpSession session,@Valid @ModelAttribute("hostname") Hostname hostname, BindingResult result) {

        try {
            Runtime.getRuntime().exec("cmd /c del pcPrinters.txt");
            Runtime.getRuntime().exec("cmd /c del pcMonitor.txt");
            Runtime.getRuntime().exec("cmd /c del pcSerial.txt");
            Runtime.getRuntime().exec("cscript pcGetInfo.vbs "+ hostname.getHostname());
            TimeUnit.SECONDS.sleep(5);
           
        } catch (Exception ex) {
            ex.printStackTrace();
            
        } 
       try {
        String pcSerial1 = retrieveFromTxt("pcSerial.txt");
       
        System.out.println(pcSerial1);
        session.setAttribute("pcSerial",pcSerial1);
        return "redirect:/results";
       }catch(Exception e) {return null;}
        
	}

	
	 public static String retrieveFromTxt(String path) throws Exception
	    {
	        String buffer = "";
	        File myFile = new File (path);
					BufferedReader bf = new BufferedReader(new FileReader(myFile));
					String details = bf.readLine();
					
					while (details != null)
					{
						
						buffer+= details + "\n";
						details = bf.readLine();
					}
					
					bf.close();
	                                return buffer;
	    }
	 
	
	
	@RequestMapping("/results")
	public String result(HttpSession session, Model model)
	{
		model.addAttribute("hostname", session.getAttribute("pcSerial"));
		return "output.jsp";
	}
}