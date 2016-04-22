package contractors

import auth.model.OrganizationFormDto
import contractors.model.{Contractor, ContractorId}
import org.joda.time.DateTime
import play.api.libs.json.Json

case class ContractorsListRecord(id: ContractorId, name: String, nip: String, address: String)

object ContractorsListRecord {

  implicit val contractorsListRecordWrites = Json.writes[ContractorsListRecord]

  def fromContractor(contractor: Contractor) =
    ContractorsListRecord(
      id = contractor.id,
      name = contractor.name,
      nip = contractor.nip,
      address = s"${contractor.address1}, ${contractor.postalCity}")
}

//TODO add pagination etc
case class ContractorsList(data: Seq[ContractorsListRecord])

object ContractorsList {

  implicit val contractorsListWrites = Json.writes[ContractorsList]

}

case class AutocompleterContractorsList(data: Seq[Contractor])

object AutocompleterContractorsList {

  implicit val autocompleterContractorsListWrites = Json.writes[AutocompleterContractorsList]

}

case class ContractorRequest(name: String,
  nip: String,
  address1: String,
  address2: Option[String] = None,
  postalCode: String,
  postalCity: String)

object ContractorRequest {

  implicit val contractorRequestReads = Json.reads[ContractorRequest]

}

case class ContractorResponse(timestamp: DateTime,
  searchIndex: Seq[String],
  name: String,
  nip: String,
  address1: String,
  address2: Option[String],
  postalCode: String,
  postalCity: String)

object ContractorResponse {

  import core.CustomDateTimeFormat._
  implicit val contractorResponseWrites = Json.writes[ContractorResponse]

  implicit def fromBe(contractor: Contractor): ContractorResponse = ContractorResponse(
    timestamp = contractor.timestamp,
    searchIndex = contractor.searchIndex,
    name = contractor.name,
    nip = contractor.nip,
    address1 = contractor.address1,
    address2 = contractor.address2,
    postalCode = contractor.postalCode,
    postalCity = contractor.postalCity)

}