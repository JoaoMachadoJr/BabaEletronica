**Aplicativo de babá Eletrônica**
=============
#####Nosso aplicativo irá servir como uma babá eletrônica.
#####Usaremos o microfone do android para captar o choro da criança e enviar notificações aos pais.
#####Para que os aparelhos saibam os IPs uns dos outros é possível que usemos um Web Service para login e logout, faremos isso se não encontrarmos uma solução mais simples antes.

![Driagrama Simples](http://i.imgur.com/YXTYCKF.png)

###**Tela Inicial**
A tela Inicial  é a primeira a ser aberta ao clicar no aplicativo.
A principal função dela é oferecer a escolha de usar no modo para crianças ou para os pais.
Como é a primeira tela a ser exibida, também deve ser a mais bonita e bem feita.

**Já feito:**

* Um botão para o Modo Criança, com o evento funcionando e abrindo uma activity para o modo criança

**Falta fazer:**

* Falta fazer o design da tela. Ela está feia e primitiva. 
* Falta chamar uma activity para o modo dos pais
* Levar direto para a activity correta, caso o usuário tenha aberto antes algum modo, iniciado o serviço, e fechado o aplicativo. Como o serviço continuará rodando, deve-se levar direto para o activity do modo em execução

###**Tela Criança**
Aqui estará a interface de uso do aplicativo para o celular que for ficar perto da criança. O mais importante é ter capacidade de ligar e desligar o monitoramento. Assim como algumas configurações de rede, a serem definidas no futuro.

**Já feito:**

* Capacidade de exibir e esconder as configurações de rede, por um evento do switch. (Embora talvez tenha feito atoa, quem for fazer o design talvez queira colocar aquilo numa tela de OPÇÕES)
* Capacidade de disparar um serviço
* Um método para capturar o IP local do usuário. (O IP da internet não é simples de capturar, pois o WIFI/Roteador/Modem mascara ele)
* Colocar listeners nos switches

**Falta fazer:**

* Fazer um design bonito e simples para a tela.
* Caso formos usar um Web Service, oferecer campos para login.
* Passar e receber configurações do Serviço Criança.

###**Tela pais**
Aqui estará a interface para o aparelho que for ficar com os pais. Deve ser capaz de escolher a sensibilidade desejada, o tipo de notificação (audio, silenciosa, etc). E ter capacidade de dizer logar no web service como ouvinte.

**Já feito:**

* Capacidade de disparar um serviço
* Um método para capturar o IP local do usuário.
* 
**Falta Fazer:**

* Capacidade de exibir e esconder as configurações de rede.
* Fazer um design bonito e simples para a tela.
* Caso formos usar um Web Service, oferecer campos para login.
* Passar e receber configurações do Serviço Pais.

###**Serviço Criança**
Esse será o serviço a ser iniciado na Tela Criança. Sua função é gravar audios, medir volume, e enviar notificações para o Serviço Pais.

**Já feito:**

* A classe já foi crianda
* Já conseguimos medir o volume ambiente :)
* Falta realizar a tarefa de medir volume em uma thread separada.
* Fazer um loop para estar sempre medindo o volume.
* Ser capaz de enviar notificações para o Serviço Pais.

**Falta fazer:**

* (Talvez) Ter diferentes opções de frequencia escuta, para economizar bateria

###**Serviço Pais**
Esse será o serviço a ser iniciado na Tela Pais. Sua função é receber notificações do serviço criança.

**Já feito:**
* Criar a classe
* Criar uma Thread para ficar ouvindo eventos do Serviço Criança
* Ser capaz de enviar para o Serviço Criança alguns dados de configurações (Sensibilidade, etc)
**Falta fazer:**

*Trabalhar como que a notificação é exibida. (Um alarme? Uma notifiaão android?, etc)


###**Web Service ou semelhante**

**Já feito:**

**Falta fazer:**

* Falta definir qual tecnologia usaremos para dizer aos telefones com quem eles irão se comunicar, IP e Porta.
* Dependendo da tecnologia a comunicação será cliente-servidor-cliente. Dependendo da tecnologia será peer-to-peer hibrido com cliente-servidor.

###**Diversos**

* Falta melhorar esse Read-me.
* Falta fazer um ícone para o aplicativo
* Falta colocar o aplicativo no Google Play

###**Orientações Gerais**
* Qualquer tarefa pesada deve ser feita em uma Thread separada para não congelar a interface.
* Qualquer tarefa que tenha risco real de causar exceção deve ser feita em uma Thread separada para não derrubar o APP e os serviços.
* Se você gostou de algum dos itens acima e quiser fazer, fique avontade :)
