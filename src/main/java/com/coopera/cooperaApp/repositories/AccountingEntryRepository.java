package com.coopera.cooperaApp.repositories;

import com.coopera.cooperaApp.models.AccountingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountingEntryRepository extends JpaRepository<AccountingEntry, String> {

}
