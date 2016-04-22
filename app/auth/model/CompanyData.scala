package auth.model

import org.joda.time.DateTime
import play.api.libs.json.Json

case class CompanyData(id: CompanyId,
                       form: CompanyForm, //osoba prawna czy fizyczna
                       name: String,
                       nip: String,
                       regon: String,
                       country: String,
                       voivodeship: String,
                       district: String, //powait
                       commune: String, //gmina
                       city: String,
                       postalCode: String,
                       postalCity: String,
                       street: Option[String],
                       buildingNr: String,
                       apartmentNr: Option[String],
                       untypicalPlace: Option[String],
                       creationDate: DateTime, //data powstania
                       startDate: DateTime //data rozpoczęcia działalnośc
  ) {

  def composeAddress1: Option[String] = Some {
    val apartmentAppendix = apartmentNr.map { ap => s" lok. $ap" }.getOrElse("")
    street match {
      case Some(street) =>
        s"$street $buildingNr$apartmentAppendix"
      case None =>
        s"$city $buildingNr$apartmentAppendix"
    }
  }

  def composeAddress2: Option[String] = Some(s"$postalCode $postalCity")

}

object CompanyData {

  implicit val organizationDataFormat = Json.format[CompanyData]

  implicit def fromEntity(od: CompanyDataEntity): CompanyData = {
    CompanyData(
      id = od._id,
      form = od.form,
      name = od.name,
      nip = od.nip,
      regon = od.regon,
      country = od.country,
      voivodeship = od.voivodeship,
      district = od.district,
      commune = od.commune,
      city = od.city,
      postalCode = od.postalCode,
      postalCity = od.postalCity,
      street = od.street,
      buildingNr = od.buildingNr,
      apartmentNr = od.apartmentNr,
      untypicalPlace = od.untypicalPlace,
      creationDate = od.creationDate,
      startDate = od.startDate)
  }

  implicit def fromRequest(od: CompanyDataDto)(implicit identity: Identity): CompanyData = {
    CompanyData(
      id = identity.company.id,
      form = CompanyForm(od.organizationForm.code),
      name = od.name,
      nip = od.nip,
      regon = od.regon,
      country = od.country,
      voivodeship = od.voivodeship,
      district = od.district,
      commune = od.commune,
      city = od.city,
      postalCode = od.postalCode,
      postalCity = od.postalCity,
      street = od.street,
      buildingNr = od.buildingNr,
      apartmentNr = od.apartmentNr,
      untypicalPlace = od.untypicalPlace,
      creationDate = od.creationDate,
      startDate = od.startDate)
  }

  implicit def fromOption(option: Option[CompanyDataEntity]): Option[CompanyData] =
    option.map(fromEntity(_))
}