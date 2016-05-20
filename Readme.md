**Aplicativo de bab� Eletr�nica**
=============
#####Nosso aplicativo ir� servir como uma bab� eletr�nica.
#####Usaremos o microfone do android para captar o choro da crian�a e enviar notifica��es aos pais.
#####Para que os aparelhos saibam os IPs uns dos outros � poss�vel que usemos um Web Service para login e logout, faremos isso se n�o encontrarmos uma solu��o mais simples antes.

![Driagrama Simples](http://i.imgur.com/YXTYCKF.png)

###**Tela Inicial**
A tela Inicial  � a primeira a ser aberta ao clicar no aplicativo.
A principal fun��o dela � oferecer a escolha de usar no modo para crian�as ou para os pais.
Como � a primeira tela a ser exibida, tamb�m deve ser a mais bonita e bem feita.
**J� feito:**

* Um bot�o para o Modo Crian�a, com o evento funcionando e abrindo uma activity para o modo crian�a

**Falta fazer:**

* Falta fazer o design da tela. Ela est� feia e primitiva. 
* Falta chamar uma activity para o modo dos pais
* Levar direto para a activity correta, caso o usu�rio tenha aberto antes algum modo, iniciado o servi�o, e fechado o aplicativo. Como o servi�o continuar� rodando, deve-se levar direto para o activity do modo em execu��o

###**Tela Crian�a**
Aqui estar� a interface de uso do aplicativo para o celular que for ficar perto da crian�a. O mais importante � ter capacidade de ligar e desligar o monitoramento. Assim como algumas configura��es de rede, a serem definidas no futuro.
**J� feito:**

* Capacidade de exibir e esconder as configura��es de rede, por um evento do switch. (Embora talvez tenha feito atoa, quem for fazer o design talvez queira colocar aquilo numa tela de OP��ES)
* Capacidade de disparar um servi�o
* Um m�todo para capturar o IP local do usu�rio. (O IP da internet n�o � simples de capturar, pois o WIFI/Roteador/Modem mascara ele)
* Colocar listeners nos switches

**Falta fazer:**

* Fazer um design bonito e simples para a tela.
* Caso formos usar um Web Service, oferecer campos para login.
* Passar e receber configura��es do Servi�o Crian�a.

###**Tela pais**
Aqui estar� a interface para o aparelho que for ficar com os pais. Deve ser capaz de escolher a sensibilidade desejada, o tipo de notifica��o (audio, silenciosa, etc). E ter capacidade de dizer logar no web service como ouvinte.
**J� feito:**
**Falta Fazer:**

* Capacidade de exibir e esconder as configura��es de rede.
* Capacidade de disparar um servi�o
* Um m�todo para capturar o IP local do usu�rio.
* Fazer um design bonito e simples para a tela.
* Caso formos usar um Web Service, oferecer campos para login.
* Passar e receber configura��es do Servi�o Pais.

###**Servi�o Crian�a**
Esse ser� o servi�o a ser iniciado na Tela Crian�a. Sua fun��o � gravar audios, medir volume, e enviar notifica��es para o Servi�o Pais.
**J� feito:**

* A classe j� foi crianda
* J� conseguimos medir o volume ambiente :)

**Falta fazer:**

* Falta realizar a tarefa de medir volume em uma thread separada.
* (Talvez) Ter diferentes op��es de frequencia escuta, para economizar bateria
* Fazer um loop para estar sempre medindo o volume.
* Ser capaz de enviar notifica��es para o Servi�o Pais.

###**Servi�o Pais**
Esse ser� o servi�o a ser iniciado na Tela Pais. Sua fun��o � receber notifica��es do servi�o crian�a.
**J� feito:**
**Falta fazer:**

* Criar a classe
* Criar uma Thread para ficar ouvindo eventos do Servi�o Crian�a
* Ser capaz de enviar para o Servi�o Crian�a alguns dados de configura��es (Sensibilidade, etc)

###**Web Service ou semelhante**
**J� feito:**
**Falta fazer:**

* Falta definir qual tecnologia usaremos para dizer aos telefones com quem eles ir�o se comunicar, IP e Porta.
* Dependendo da tecnologia a comunica��o ser� cliente-servidor-cliente. Dependendo da tecnologia ser� peer-to-peer hibrido com cliente-servidor.

###**Diversos**

* Falta melhorar esse Read-me.
* Falta fazer um �cone para o aplicativo
* Falta colocar o aplicativo no Google Play

###**Orienta��es Gerais**
* Qualquer tarefa pesada deve ser feita em uma Thread separada para n�o congelar a interface.
* Qualquer tarefa que tenha risco real de causar exce��o deve ser feita em uma Thread separada para n�o derrubar o APP e os servi�os.
* Se voc� gostou de algum dos itens acima e quiser fazer, fique avontade :)