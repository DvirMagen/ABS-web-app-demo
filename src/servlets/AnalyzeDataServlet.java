package servlets;

import com.google.gson.Gson;
import engine.Loan;
import engine.Xml;
import engine.myExceptions.CategoryDoesNotExistException;
import engine.myExceptions.LoanDoesNotPayEveryYazTimeCorrectlyException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import listener.ContextListener;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static constants.Constants.USERNAME;

public class AnalyzeDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int flag = 0;
        System.out.println("AnalyzeDataServlet doGet");
        System.out.println(req.toString());
        String usernameFromParameter = req.getParameter(USERNAME);
        System.out.println(usernameFromParameter);
        String xmlPathFromParameter = req.getParameter("xmlPath");
        System.out.println(xmlPathFromParameter);
        int status = 200;
        try {
            status= ContextListener.engine.loadDataFromClientXmlFile(xmlPathFromParameter,usernameFromParameter);
        } catch (CategoryDoesNotExistException e) {
            status = 400;
            flag = 2;
            //throw new RuntimeException(e);
        } catch (LoanDoesNotPayEveryYazTimeCorrectlyException e) {
            status= 400;
            flag = 1;
//            throw new RuntimeException(e);
        } catch (JAXBException e) {
            status = 400;
            flag = 3;
        }
        finally {
            if (status == 200) {
//                resp.setContentType("application/json");
//                try (PrintWriter out = resp.getWriter()) {
//                    Gson gson = new Gson();
//                    List<Loan> allLoans = ContextListener.engine.getAllLoans();
//                    String json = gson.toJson(allLoans);
//                    out.println(json);
//                    out.flush();
//                }
            }else{
                resp.setStatus(status);
                resp.setContentType("application/json");
                try (PrintWriter out = resp.getWriter()) {
                    Gson gson = new Gson();
                    HashMap<String,String> map = new HashMap<>();
                    if (status == 400)
                        if (flag == 0)
                            map.put("Error msg","XML already analyzed");
                        else if(flag==1)
                            map.put("Error msg","XML Loan Container Does Not Pay Every Yaz Time Correct");
                        else if(flag == 2)
                            map.put("Error msg","XML Category container does not exist!");
                        else if(flag == 3)
                            map.put("Error msg", "XML is not valid!");

                    else{
                        map.put("Error msg","File been corrupted");
                    }
                    String json = gson.toJson(map);
                    out.println(json);
                    out.flush();
                }


            }
        }



    }
}
