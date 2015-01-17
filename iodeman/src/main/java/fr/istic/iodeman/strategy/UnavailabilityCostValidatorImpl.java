package fr.istic.iodeman.strategy;

import java.util.Collection;

import org.apache.commons.lang.Validate;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

public class UnavailabilityCostValidatorImpl implements UnavailabilityCostValidator {
	
	private Collection<Priority> priorities;
	private Collection<Unavailability> notRespectedUA;
	private Integer cost;

	public void configure(Collection<Priority> priorities) {
	
		this.priorities = Lists.newArrayList();
		this.notRespectedUA = Lists.newArrayList();

		if (priorities != null) {
			this.priorities.addAll(priorities);
		}
		
	}
	
	public int execute(Collection<OralDefense> oralDefenses,
			Collection<Unavailability> unavailabilities) {
		
		// Verify that this algorithm has been configured properly
		Validate.notNull(priorities);
		Validate.notNull(notRespectedUA);
		
		cost = 0;
		
		for(final OralDefense oralDefense : oralDefenses) {
			
			// filter the unavaibilities to get only the ones matching the current oral defense
			Collection<Unavailability> uaMatched = Collections2.filter(unavailabilities, new Predicate<Unavailability>() {
				public boolean apply(Unavailability a) {
					Person p = a.getPerson();
					return oralDefense.getComposition().getStudent().equals(p)
							|| oralDefense.getComposition().getFollowingTeacher().equals(p);
				}
			});
			
			for (Unavailability ua : uaMatched) {
				
				if (!AlgoPlanningUtils.isAvailable(ua, oralDefense.getTimebox())) {
					
					// register the UA that has not been respected
					notRespectedUA.add(ua);
					cost += findCost(ua.getPerson().getRole());
				
				}
				
			}
			
		}
		
		return cost;
		
	}
	
	private int findCost(Role role) {
		
		for(Priority p : priorities) {
			if (p.getRole() == role) {
				return p.getWeight();
			}
		}
		
		return 0;
		
	}

	public int getCost() {
		
		return cost;
	}

	public Collection<Unavailability> getNotRespectedUnavailabilities() {
		
		return notRespectedUA;
	}

}
