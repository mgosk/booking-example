package auth.model

import org.joda.time.DateTime
import play.api.libs.json.Json

case class CompanyDataEntity(_id: CompanyId,
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
  )

object CompanyDataEntity {

  implicit val organizationDataEntityFormat = Json.format[CompanyDataEntity]

  implicit def fromBe(od: CompanyData): CompanyDataEntity = {
    CompanyDataEntity(
      _id = od.id,
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
}