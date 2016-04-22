package gus

import gus.model.GusReportType

class Requests(endpointAddress: String, key: String) {

  lazy val zaloguj = <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ns="http://CIS/BIR/PUBL/2014/07">
    <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
      <wsa:To>{ endpointAddress }</wsa:To>
      <wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Zaloguj</wsa:Action>
    </soap:Header>
    <soap:Body>
      <ns:Zaloguj>
        <ns:pKluczUzytkownika>{ key }</ns:pKluczUzytkownika>
      </ns:Zaloguj>
    </soap:Body>
  </soap:Envelope>

  def getValue(param: String) = <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ns="http://CIS/BIR/2014/07">
    <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
      <wsa:To>{ endpointAddress }</wsa:To>
      <wsa:Action>http://CIS/BIR/2014/07/IUslugaBIR/GetValue</wsa:Action>
    </soap:Header>
    <soap:Body>
      <ns:GetValue>
        <ns:pNazwaParametru>{ param }</ns:pNazwaParametru>
      </ns:GetValue>
    </soap:Body>
  </soap:Envelope>

  def daneSzukaj(nip: String) = <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ns="http://CIS/BIR/PUBL/2014/07" xmlns:dat="http://CIS/BIR/PUBL/2014/07/DataContract">
    <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
      <wsa:To>{ endpointAddress }</wsa:To>
      <wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/DaneSzukaj</wsa:Action>
    </soap:Header>
    <soap:Body>
      <ns:DaneSzukaj>
        <ns:pParametryWyszukiwania>
          <!--Optional:-->
          <dat:Krs></dat:Krs>
          <!--Optional:-->
          <dat:Krsy></dat:Krsy>
          <!--Optional:-->
          <dat:Nip>{ nip }</dat:Nip>
          <!--Optional:-->
          <dat:Nipy></dat:Nipy>
          <!--Optional:-->
          <dat:Regon></dat:Regon>
          <!--Optional:-->
          <dat:Regony14zn></dat:Regony14zn>
          <!--Optional:-->
          <dat:Regony9zn></dat:Regony9zn>
        </ns:pParametryWyszukiwania>
      </ns:DaneSzukaj>
    </soap:Body>
  </soap:Envelope>

  def danePobierzPelnyRaport(regon: String, raportGus: GusReportType) = <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ns="http://CIS/BIR/PUBL/2014/07">
      <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
        <wsa:To>{ endpointAddress }</wsa:To>
        <wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/DanePobierzPelnyRaport</wsa:Action>
      </soap:Header>
      <soap:Body>
        <ns:DanePobierzPelnyRaport>
          <ns:pRegon>{ regon }</ns:pRegon>
          <ns:pNazwaRaportu>{ raportGus.toString }</ns:pNazwaRaportu>
        </ns:DanePobierzPelnyRaport>
      </soap:Body>
    </soap:Envelope>

  lazy val pobierzCaptcha = <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ns="http://CIS/BIR/2014/07">
    <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
      <wsa:To>{ endpointAddress }</wsa:To>
      <wsa:Action>http://CIS/BIR/2014/07/IUslugaBIR/PobierzCaptcha</wsa:Action>
    </soap:Header>
    <soap:Body>
      <ns:PobierzCaptcha/>
    </soap:Body>
  </soap:Envelope>

  def sprawdzCaptcha(captcha: String) = <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:ns="http://CIS/BIR/2014/07">
    <soap:Header xmlns:wsa="http://www.w3.org/2005/08/addressing">
      <wsa:To>{ endpointAddress }</wsa:To>
      <wsa:Action>http://CIS/BIR/2014/07/IUslugaBIR/SprawdzCaptcha</wsa:Action>
    </soap:Header>
    <soap:Body>
      <ns:SprawdzCaptcha>
        <ns:pCaptcha>{ captcha }</ns:pCaptcha>
      </ns:SprawdzCaptcha>
    </soap:Body>
  </soap:Envelope>

}
