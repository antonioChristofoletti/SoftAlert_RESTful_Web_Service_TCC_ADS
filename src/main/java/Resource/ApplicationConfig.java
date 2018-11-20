package Resource;

import ExceptionSistema.BancoDeDadosExceptionMapper;
import ExceptionSistema.CampoIncorretoExceptionMapper;
import ExceptionSistema.GenericExceptionMapper;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.jackson.JacksonFeature;

@javax.ws.rs.ApplicationPath("")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();

        addRestResourceClasses(resources);

        resources.add(JacksonFeature.class);
        resources.add(CampoIncorretoExceptionMapper.class);
        resources.add(BancoDeDadosExceptionMapper.class);
        resources.add(GenericExceptionMapper.class);

        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(Controller.GenericResource.class);
        resources.add(ExceptionSistema.JsonMappingExceptionMapper.class);
        resources.add(ExceptionSistema.JsonParseExceptionMapper.class);
        resources.add(ExceptionSistema.JsonUnrecognizedPropertyExceptionMapper.class);
        resources.add(Resource.AlertaResource.class);
        resources.add(Resource.AlertaUsuarioClienteResource.class);
        resources.add(Resource.Autenticacao.AutenticacaoResource.class);
        resources.add(Resource.Autenticacao.FiltroAutenticacao.class);
        resources.add(Resource.EnderecoUsuarioResource.class);
        resources.add(Resource.LocalizacaoAlertaResource.class);
        resources.add(Resource.NivelAlertaResource.class);
        resources.add(Resource.UsuarioAdministradorResource.class);
        resources.add(Resource.UsuarioClienteResource.class);
    }
}
