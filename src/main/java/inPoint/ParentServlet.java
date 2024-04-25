package inPoint;

import core.createDTO.ParentCreateDTO;
import core.viewDTO.ParentViewDTO;
import dao.ds.fabric.DataSourceSingleton;
import dao.fabric.AddressDaoSingleton;
import dao.fabric.ParentDaoSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.api.IParentService;
import service.fabric.AddressServiceSingleton;
import service.fabric.ParentServiceSingleton;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Сервлет для создания, обновления, удаления, получения родителей
 */
@WebServlet(name = "parent_servlet", urlPatterns = "/parent")
public class ParentServlet extends HttpServlet {
    private final IParentService parentService;
    private final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private final String CHARACTER_ENCODING = "UTF-8";

    public ParentServlet() throws PropertyVetoException {
        this.parentService = ParentServiceSingleton.getInstance(
                ParentDaoSingleton.getInstance(DataSourceSingleton.getInstance()),
                AddressServiceSingleton.getInstance(AddressDaoSingleton.getInstance(DataSourceSingleton.getInstance()))
        );
    }

    /**
     * Получение родителей
     * Param id - получение адреса по id
     * param id = null - получение всех родителей

     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @retun {@link core.viewDTO.ParentViewDTO}
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        PrintWriter writer = resp.getWriter();
        if(idStr!=null||!idStr.isBlank()){
            long id = Long.valueOf(idStr);
            ParentViewDTO parentViewDTO = parentService.get(id);
            if(parentViewDTO==null)
                throw new IllegalArgumentException("Не верно введен id");
            writer.write(parentViewDTO.toString());
        } else {
            List<ParentViewDTO> parents = parentService.getAll();
            for(ParentViewDTO parent : parents){
                writer.write(parent.toString());
            }
        }
    }

    /**
     * создание родителя:
     * param: name, {idAddress}
     * преобразование в {@link core.createDTO.ParentCreateDTO}
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);
        parentService.create(getParentCreateDTO(req));
    }

    /**
     * Изменение родителя
     * param: id, version, name, idAddress
     * преобразование в {@link core.createDTO.ParentCreateDTO}
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        String versionSrt = req.getParameter("version");
        validation(idStr);
        validation(versionSrt);
        long id = Long.valueOf(idStr);
        long version = Long.valueOf(versionSrt);
        ParentCreateDTO parentCreateDTO = getParentCreateDTO(req);
        parentService.update(id,version,parentCreateDTO);
    }

    /**
     * Удаление адреса
     * param: id, version
     * @param req the {@link HttpServletRequest} object that contains the request the client made of the servlet
     *
     * @param resp the {@link HttpServletResponse} object that contains the response the servlet returns to the client
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        String versionStr = req.getParameter("version");
        validation(idStr);
        validation(versionStr);
        long id = Long.valueOf(idStr);
        long version = Long.valueOf(versionStr);
        parentService.delete(id,version);
    }

    private void validation(String string){
        if(string==null||string.isEmpty()||string.isBlank())
            throw new IllegalArgumentException("Не верно введены данные");
    }

    private ParentCreateDTO getParentCreateDTO(HttpServletRequest req){
        String name = req.getParameter("name");
        validation(name);
        String address = req.getParameter("address");
        validation(address);
        long addressId = Long.valueOf(address);
        return new ParentCreateDTO(name,addressId);
    }
}
