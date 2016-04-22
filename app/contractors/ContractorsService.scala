package contractors

import javax.inject.Inject

import auth.model.{Identity, User}
import contractors.model.{Contractor, ContractorId}
import core.model.State

import scala.concurrent.Future

class ContractorsService @Inject() (contractorsManager: ContractorsManager) {

  def create(contractor: Contractor): Future[Contractor] =
    contractorsManager.create(contractor)

  def update(contractor: Contractor)(implicit identity: Identity): Future[Contractor] =
    contractorsManager.update(contractor)

  def find(id: ContractorId)(implicit identity: Identity): Future[Option[Contractor]] =
    contractorsManager.find(id)

  def find(sortKey: String, sortReverse: Boolean)(implicit identity: Identity): Future[Seq[Contractor]] =
    contractorsManager.find(sortKey, sortReverse)

  def remove(contractor: Contractor)(implicit identity: Identity): Future[Contractor] =
    contractorsManager.update(contractor.copy(state = State.Archived))

  def findByTerm(term: String)(implicit identity: Identity): Future[Seq[Contractor]] =
    contractorsManager.findByTerm(term)

}
