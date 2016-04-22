package contractors.model

import auth.model.{CompanyId, Identity, User, UserId}
import contractors.ContractorRequest
import core.model.State
import org.joda.time.DateTime
import play.api.libs.json.Json

case class Contractor(id: ContractorId,
  version: ContractorVersion,
  owner: CompanyId,
  timestamp: DateTime,
  searchIndex: Seq[String],
  name: String,
  nip: String,
  address1: String,
  address2: Option[String],
  postalCode: String,
  postalCity: String,
  state: State) {

  def composePostal: Option[String] = Some(s"$postalCode $postalCity")

}

object Contractor {

  implicit val contractorFormat = Json.format[Contractor]

  def fromRequest(contractor: ContractorRequest, id: Option[ContractorId] = None)(implicit identity: Identity): Contractor = Contractor(
    id = id.getOrElse(ContractorId.random),
    version = ContractorVersion.random,
    owner = identity.company.id,
    timestamp = DateTime.now(),
    searchIndex = Seq(contractor.name, contractor.nip),
    name = contractor.name,
    nip = contractor.nip,
    address1 = contractor.address1,
    address2 = contractor.address2,
    postalCode = contractor.postalCode,
    postalCity = contractor.postalCity,
    state = State.Active)

  implicit def fromEntity(contractor: ContractorEntity): Contractor = Contractor(
    id = contractor._id,
    version = contractor.version,
    owner = contractor.owner,
    timestamp = contractor.timestamp,
    searchIndex = contractor.searchIndex,
    name = contractor.name,
    nip = contractor.nip,
    address1 = contractor.address1,
    address2 = contractor.address2,
    postalCode = contractor.postalCode,
    postalCity = contractor.postalCity,
    state = contractor.state)

  implicit def fromOption(option: Option[ContractorEntity]): Option[Contractor] = option.map(fromEntity(_))

  implicit def fromSeq(seq: Seq[ContractorEntity]): Seq[Contractor] = seq.map(fromEntity(_))

}



case class ContractorEntity(_id: ContractorId,
                            version: ContractorVersion,
                            owner: CompanyId,
                            timestamp: DateTime,
                            searchIndex: Seq[String],
                            name: String,
                            nip: String,
                            address1: String,
                            address2: Option[String],
                            postalCode: String,
                            postalCity: String,
                            state: State)

object ContractorEntity {

  implicit val contractorEntityFormat = Json.format[ContractorEntity]

  implicit def fromBe(contractor: Contractor): ContractorEntity = ContractorEntity(
    _id = contractor.id,
    version = contractor.version,
    owner = contractor.owner,
    timestamp = contractor.timestamp,
    searchIndex = contractor.searchIndex,
    name = contractor.name,
    nip = contractor.nip,
    address1 = contractor.address1,
    address2 = contractor.address2,
    postalCode = contractor.postalCode,
    postalCity = contractor.postalCity,
    state = contractor.state)

}

