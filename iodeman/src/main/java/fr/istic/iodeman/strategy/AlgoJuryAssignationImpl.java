package fr.istic.iodeman.strategy;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

public class AlgoJuryAssignationImpl implements AlgoJuryAssignation {

	private Collection<OralDefense> oralDefenses;
	private Collection<Unavailability> unavailabilities;
	
	private Map<Person, List<OralDefense>> followings;
	private Map<Person, List<OralDefense>> assignations;
	
	private Collection<AssignationCandidate> candidates;
	
	private boolean hasNewAssignation = false;
	
	private class AssignationCandidate {
		
		private OralDefense oralDefense;
		private List<Person> possibleJurys;
		
		public AssignationCandidate(OralDefense oralDefense) {
			this.oralDefense = oralDefense;
			this.possibleJurys = Lists.newArrayList();
		}
		
		public OralDefense getOralDefense() {
			return oralDefense;
		}
		
		public List<Person> getPossibleJurys() {
			return possibleJurys;
		}
		
	}
	
	public void configure(Collection<OralDefense> oralDefenses, Collection<Unavailability> unavailabilities) {
		
		this.oralDefenses = Lists.newArrayList();
		this.unavailabilities = Lists.newArrayList();
		this.followings = Maps.newHashMap();
		this.assignations = Maps.newHashMap();
		this.candidates = Lists.newArrayList();
		
		if (oralDefenses != null) {
			this.oralDefenses.addAll(oralDefenses);
		}
		
		if (unavailabilities != null) {
			this.unavailabilities.addAll(unavailabilities);
		}
		
		// Collections initialisations
		for(OralDefense od : oralDefenses) {
			
			Person p = od.getComposition().getFollowingTeacher();
			List<OralDefense> fl = followings.get(p);
			if (fl == null) {
				fl = Lists.newArrayList();
				followings.put(p, fl);
			}
			fl.add(od);
			
			candidates.add(new AssignationCandidate(od));
			
		}

		// Sort the list of candidates by the number of oral defenses followed
		Comparator<AssignationCandidate> byNbFollowings = new Comparator<AssignationCandidate>() {
			public int compare(AssignationCandidate a1, AssignationCandidate a2) {
				Integer nb1 = followings.get(a1.getOralDefense().getComposition().getFollowingTeacher()).size();
				Integer nb2 = followings.get(a2.getOralDefense().getComposition().getFollowingTeacher()).size();
				return nb2.compareTo(nb1);
			}
		};
		candidates = Ordering.from(byNbFollowings).sortedCopy(candidates);
		
	}

	public Collection<OralDefense> execute() {
		
		Validate.notNull(oralDefenses);
		Validate.notNull(unavailabilities);
		Validate.notNull(followings);
		Validate.notNull(assignations);
		Validate.notNull(candidates);
		
		while (!candidates.isEmpty()) {
			
			hasNewAssignation = true;
			
			while(hasNewAssignation) {
			
				hasNewAssignation = false;
				
				for(AssignationCandidate candidat : Lists.newArrayList(candidates)) {
					
					System.out.println("try allocation for the oral defense of "
							+candidat.getOralDefense().getComposition().getStudent().getFirstName());
					tryAllocation(candidat, false);
					
				}
			
			}
			
			if (!candidates.isEmpty()) {
				
				forceAllocation();
				
				if (!hasNewAssignation) {
					// Cannot force allocation
					return oralDefenses;
				}
			}
			
		}
		
		return oralDefenses;
		
	}
	
	private void tryAllocation(final AssignationCandidate candidate, final boolean withUnavailabilities) {
		
		Collection<Person> possibilities = Collections2.filter(followings.keySet(), new Predicate<Person>() {
			
			public boolean apply(final Person jury) {
				
				/*Integer nbAssignations = 0;
				List<OralDefense> juryAssignations = assignations.get(jury);
				if (juryAssignations != null) {
					nbAssignations = juryAssignations.size();
					// check if the jury is not assignated to another oral defense on this timebox
					for(OralDefense assignated : juryAssignations) {
						if (candidate.getOralDefense().getTimebox().getFrom().equals(assignated.getTimebox().getFrom())) {
							System.out.println("remove "+jury.getFirstName());
							return false;
						}
					}
				}
				
				List<OralDefense> juryFollowings = followings.get(jury);
				Integer nbFollowings = juryFollowings.size();
				// check if the jury is not present at another oral defense as follower on this timebox
				for(OralDefense followed : juryFollowings) {
					if (candidate.getOralDefense().getTimebox().getFrom().equals(followed.getTimebox().getFrom())) {
						System.out.println("remove "+jury.getFirstName());
						return false;
					}
				}*/
				
				List<OralDefense> juryAssignations = assignations.get(jury);
				Integer nbAssignations = juryAssignations != null ? juryAssignations.size() : 0;
				List<OralDefense> juryFollowings = followings.get(jury);
				Integer nbFollowings = juryFollowings.size();
				
				if (!isFree(candidate.getOralDefense().getTimebox(), jury)) {
					System.out.println("remove "+jury.getFirstName());
					return false;
				}
				
				if (withUnavailabilities) {
					
					Collection<Unavailability> uaJury = Collections2.filter(unavailabilities, new Predicate<Unavailability>() {
						public boolean apply(Unavailability ua) {
							return ua.getPerson().equals(jury);
						}
					});

					if (!AlgoPlanningUtils.isAvailable(uaJury, candidate.getOralDefense().getTimebox())) {
						return false;
					}
					
				}
				
				return (!jury.equals(candidate.getOralDefense().getComposition().getFollowingTeacher())
						&& nbAssignations < nbFollowings);
			}
		});
		
		System.out.println(possibilities.size()+" possibilities found!");
		
		candidate.getPossibleJurys().clear();
		candidate.getPossibleJurys().addAll(possibilities);
		
		if (candidate.getPossibleJurys().size() == 1) {
			
			assignJury(candidate, candidate.getPossibleJurys().get(0));
		
		}else if (!withUnavailabilities && !candidate.getPossibleJurys().isEmpty()) {
			
			tryAllocation(candidate, true);
		
		}
		
	}
	
	private void forceAllocation() {
		
		System.out.println("forcing allocation...");
		
		int nb = -1;
		AssignationCandidate selected = null;
		
		for(AssignationCandidate candidate : candidates) {
			if (nb == -1 || (candidate.getPossibleJurys().size() < nb && nb > 0)) {
				selected = candidate;
				nb = candidate.getPossibleJurys().size();
			}
		}
		
		if (selected != null) {
			if (!selected.getPossibleJurys().isEmpty()) {
				assignJury(selected, selected.getPossibleJurys().get(0));
			}else{
				// select the jury with the minimum assignations
				/*nb = -1;
				Person jury = null;
				for (Person p : assignations.keySet()) {
					int nbAssignations = assignations.get(p).size();
					if ((jury == null || nbAssignations < nb)
							&& !p.equals(selected.getOralDefense().getComposition().getFollowingTeacher())) {
						nb = nbAssignations;
						jury = p;
					}
				}*/
				Person jury = getPossibleJuryWith(selected);
				
				if (jury != null) {
					assignJury(selected, jury);
				}
			}
		}
		
	}
	
	private Person getPossibleJuryWith(AssignationCandidate selected) {
		
		Person followingTeacher = selected.getOralDefense().getComposition().getFollowingTeacher();
		
		Comparator<Person> byNbAssignations = new Comparator<Person>() {
			public int compare(Person p1, Person p2) {
				Integer nb1 = assignations.get(p1).size();
				Integer nb2 = assignations.get(p2).size();
				return nb1.compareTo(nb2);
			}
		};

		Collection<Person> possibilities = Ordering.from(byNbAssignations).sortedCopy(assignations.keySet());
		
		for(Person p : possibilities) {
			int nbAssignations = assignations.get(p).size();
			int nbFollowed = followings.get(p).size();
			if (nbAssignations < nbFollowed 
					&& !p.equals(followingTeacher)
					&& isFree(selected.getOralDefense().getTimebox(), p)) {
				return p;
			}
		}
		
		// hack to assign a jury if each jury has reached his maximum number of assignations
		for(Person p : possibilities) {
			if (!p.equals(followingTeacher) && isFree(selected.getOralDefense().getTimebox(), p)) {
				return p;
			}
		}
		
		return null;
	}
	
	private boolean isFree(TimeBox timebox, Person jury) {
		
		List<OralDefense> juryAssignations = assignations.get(jury);
		if (juryAssignations != null) {
			// check if the jury is not assignated to another oral defense on this timebox
			for(OralDefense assignated : juryAssignations) {
				if (timebox.getFrom().equals(assignated.getTimebox().getFrom())) {
					System.out.println("remove "+jury.getFirstName());
					return false;
				}
			}
		}
		
		List<OralDefense> juryFollowings = followings.get(jury);
		// check if the jury is not present at another oral defense as follower on this timebox
		for(OralDefense followed : juryFollowings) {
			if (timebox.getFrom().equals(followed.getTimebox().getFrom())) {
				System.out.println("remove "+jury.getFirstName());
				return false;
			}
		}
		
		return true;
	}
	
	private void assignJury(AssignationCandidate candidate, Person jury) {
		
		System.out.println("assignation of the jury "
				+jury.getFirstName()
				+" to the oral defense of "
				+candidate.getOralDefense().getComposition().getStudent().getFirstName());
		
		
		OralDefense od = candidate.getOralDefense();
		
		od.setJury(Lists.newArrayList(jury));
		
		List<OralDefense> juryAssignations = assignations.get(jury);
		if (juryAssignations == null) {
			juryAssignations = Lists.newArrayList();
			assignations.put(jury, juryAssignations);
		}
		juryAssignations.add(od);
		System.out.println("nb assignations for this jury: "+juryAssignations.size());
		
		this.candidates.remove(candidate);
		
		hasNewAssignation = true;
		
		System.out.println("oral defenses not assigned: "+candidates.size());
		
	}

}
