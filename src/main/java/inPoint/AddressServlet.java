package inPoint;

import core.createDTO.AddressCreateDTO;
import core.viewDTO.AddressViewDTO;
import dao.ds.fabric.DataSourceSingleton;
import dao.fabric.AddressDaoSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.api.IAddressService;
import service.fabric.AddressServiceSingleton;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AddressServlet", urlPatterns = "/address")
public class AddressServlet extends HttpServlet {
    private final IAddressService service;
    private final String CHARACTER_ENCODING = "UTF-8";
    private final String CONTENT_TYPE = "text/html; charset=UTF-8";


    public AddressServlet() throws PropertyVetoException {
        this.service = AddressServiceSingleton.getInstance(
                AddressDaoSingleton.getInstance(DataSourceSingleton.getInstance())
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType(CONTENT_TYPE);
        PrintWriter writer = resp.getWriter();

        String idStr = req.getParameter("id");
        if(idStr==null||!validate(idStr)){
            List<AddressViewDTO> addresses = service.getAll();
            for(AddressViewDTO address : addresses)
            writer.write("<p>"+address.toString()+"</p>");
        } else {
            long id = Long.valueOf(idStr);
            AddressViewDTO address = service.get(id);
                if(address == null)
                    throw new IllegalArgumentException("Данный адресс не найден");
            writer.write("<>"+address.toString()+"</p>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String address = req.getParameter("address");
        String numberHouseStr = req.getParameter("number_house");
        String numberFlatStr = req.getParameter("number_flat");
        if(!validate(numberFlatStr)||!validate(address)||!validate(numberHouseStr))
            throw new IllegalArgumentException("Не корректно введены данные");
        int numberHouse = Integer.valueOf(numberHouseStr);
        int numberFlat = Integer.valueOf(numberFlatStr);
        service.create(new AddressCreateDTO(address,numberFlat,numberHouse));
        PrintWriter writer = resp.getWriter();
        writer.write("OK");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        String versionStr = req.getParameter("version");
        String address = req.getParameter("address");
        String numberHouseStr = req.getParameter("number_house");
        String numberFlatStr = req.getParameter("number_flat");
        if(!validate(idStr)||!validate(versionStr)||!validate(address)||!validate(numberHouseStr)||!validate(numberFlatStr)){
            throw new IllegalArgumentException("Не верно введены данные");
        }

        long id  = Long.valueOf(idStr);
        long version = Long.valueOf(versionStr);
        int numberHouse = Integer.valueOf(numberHouseStr);
        int numberFlat = Integer.valueOf(numberFlatStr);
        service.update(id,version,
                new AddressCreateDTO(address,numberHouse,numberFlat));
        PrintWriter writer = resp.getWriter();
        writer.write("OK");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        String versionStr = req.getParameter("version");
        if(!validate(idStr)||!validate(versionStr)){
            throw new IllegalArgumentException("Не верно введены данные");
        }
        try {
            long id  = Long.valueOf(idStr);
            long version = Long.valueOf(versionStr);
            service.delete(id,version);
        } catch (NumberFormatException e){
            throw new NumberFormatException("Не корректно введны данные");
        }
        PrintWriter writer = resp.getWriter();
        writer.write("OK");
    }

    private boolean validate(String string){
        if(string==null||string.isEmpty()||string.isBlank()){
            return false;
        } else
            return true;
    }
}
