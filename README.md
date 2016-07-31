# API_Generator_MCD

API para criar classes CONTROLLER e DAO a partir de classes MODEL, utilizando TAMPLATE gerérico das classes CONTROLLER e DAO.
API to create CONTROLLER and DAO classes from MODEL classes using generic tamplate the CONTROLLER and DAO classes.

   
  
   @author Lourran
  
   Este é um Gerador de Classes. Utilizado para gerar classes com o mesmo
   Tamplate. Foi criado pois em um de meus projetos, eu precisava ficar criando
   classes do padrão MVC muito repetitivas. Atravez da pasta informada no
   argumento MODEL_PATH onde possui a classes do padrão MVC serão criadas
   classes do seguindo o CONTROLLER_TAMPLATE e DAO_TAMPLATE. 
   
   Para cada classe dentro do argumento MODEL_PATH, será gerado uma equivalente ao
   CONTROLLER_TAMPLATE e DAO_TAMPLATE Dentro do CONTROLLER_TAMPLATE e
   DAO_TAMPLATE sera necessario Substituir a parte especifica, referente a
   classe MODEL por ':TYPE'
  
   Ex: Original
  
   import br.com.achimid.model.Venda;
  
    public class DaoVenda extends DaoGenerico<Venda> implements InterfaceContador<Venda> {
  
    public DaoVenda() { super(new Venda()); }
  
    public Venda gravarVenda(Venda obj){ super.gravar(obj); }
  
    public Venda recuperaVenda(int id){ super.recupera(id); } }
  
   Ex: Tranformado em DAO_TAMPLATE
  
    import br.com.achimid.model.:TYPE;
  
    public class Dao:TYPE extends DaoGenerico<:TYPE> implements InterfaceContador<:TYPE> {
  
    public Dao:TYPE() { super(new :TYPE()); }
  
    public :TYPE gravar:TYPE(:TYPE obj){ super.gravar(obj); }
  
    public :TYPE recupera:TYPE(int id){ super.recupera(id); } }
  
   Não esqueca de Substituir nos Imports Tambem.
  
   Por padrão a classe será criada com o nomer Controller + Nome da Classe model
   EX: Classe MODEL = Venda.java -> Classe DAO = DaoVenda.java -> Classe
   CONTROLLER = ControllerVenda.java
  
   Caso necessitel alterar o padrão de nomenclatura das classes CONTROLLER e
   DAO, informe os parametros CONTROLLER_NAME_PATTERN e DAO_NAME_PATTERN e
   substituia no tamplate por :NAME_PATTERN
  
   EX:
  
    import br.com.achimid.model.:TYPE;
  
    public class :NAME_PATTERN:TYPE extends DaoGenerico<:TYPE> implements InterfaceContador<:TYPE> {
  
    public :NAME_PATTERN:TYPE() { super(new :TYPE()); }
  
    public :TYPE gravar:TYPE(:TYPE obj){ super.gravar(obj); }
  
    public :TYPE recupera:TYPE(int id){ super.recupera(id); } }
  
   Exemplo de Utilização (CMD Windows): 
    Ex1: API_Generator_MCD.jar "br.com.achimid.model" "ControllerVenda.java" "DaoVenda.java" 
    Ex2: API_Generator_MCD.jar "br.com.achimid.model" "ControllerVenda.java" "DaoVenda.java" "DAO" "CONTROLLER"
  
   SAIDA: 
   
    public class DAOItem extends DaoGenerico<Item> implements InterfaceContador<Item> {
  
    public DAOItem() { super(new Item()); }
  
    public Item gravarItem(Item obj){ super.gravar(obj); }
  
    public Item recuperaItem(int id){ super.recupera(id); } }
  
   
