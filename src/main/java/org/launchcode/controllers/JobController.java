package org.launchcode.controllers;

import org.launchcode.models.Employer;
import org.launchcode.models.Job;
import org.launchcode.models.JobFieldType;
import org.launchcode.models.Location;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, Integer id) {
        if (id == null)
            return "redirect:";


        // TODO #1 - get the Job with the given ID and pass it into the view
        Job job = jobData.findById(id);
        model.addAttribute("title", "Jobs Detail");
        model.addAttribute("job", job);
        model.addAttribute("jobId", id);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @Valid JobForm jobForm, Errors errors, RedirectAttributes redirectAttributes) {

        // TODO #6 - Validate the JobForm model.

        Job newJob = new Job();

        String name = jobForm.getName();
        newJob.setName(name);

        int employerId = jobForm.getEmployerId();
        Employer employer = jobData.getEmployers().findById(employerId);
        newJob.setEmployer(employer);

        int locationId = jobForm.getLocationId();
        Location location = jobData.getLocations().findById(locationId);
        newJob.setLocation(location);

        newJob.setPositionType(jobData.getPositionTypes().findById(jobForm.getPositionTypeId()));
        newJob.setCoreCompetency(jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId()));

            //if valid, create a new Job
            //add it to the jobData data store


        if (errors.hasErrors()) {
            model.addAttribute("job", newJob);
            model.addAttribute("title", "Add Job");
            return "new-job";
        }

        jobData.add(newJob);

        //return "redirect:/job?id="+newJob.getId();
        redirectAttributes.addAttribute("id", newJob.getId());
        return "redirect:/job";
    }
}
