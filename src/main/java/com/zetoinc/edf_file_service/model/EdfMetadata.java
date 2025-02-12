package com.zetoinc.edf_file_service.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing metadata for an EDF (European Data Format) file.
 * <p>
 * This class is mapped to the {@code edf_metadata} table in the database and
 * stores essential details about an EDF file, such as the title, patient ID,
 * number of channels, duration, annotations, and start date.
 * </p>
 */
@Entity
@Table(name = "edf_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EdfMetadata {

    /**
     * Unique identifier for the EDF metadata entry.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The title or description of the EDF file.
     */
    private String title;

    /**
     * The patient ID associated with the EDF file.
     */
    private String patientId;

    /**
     * The number of channels recorded in the EDF file.
     */
    private int numberOfChannels;

    /**
     * The total duration of the EDF recording in seconds.
     */
    private double duration;

    /**
     * The number of annotations present in the EDF file.
     */
    private int numberOfAnnotations;

    /**
     * The start date and time of the EDF recording.
     */
    private String startDate;

    /**
     * Constructor to initialize an EDF metadata object without an ID.
     * <p>
     * This constructor is useful when creating new instances before persistence.
     * </p>
     *
     * @param title             The title or description of the EDF file.
     * @param patientId         The patient ID associated with the EDF file.
     * @param numberOfChannels  The number of channels recorded in the EDF file.
     * @param duration          The total duration of the EDF recording in seconds.
     * @param numberOfAnnotations The number of annotations in the EDF file.
     * @param startDate         The start date and time of the EDF recording.
     */
    public EdfMetadata(String title, String patientId, int numberOfChannels, double duration, int numberOfAnnotations, String startDate) {
        this.title = title;
        this.patientId = patientId;
        this.numberOfChannels = numberOfChannels;
        this.duration = duration;
        this.numberOfAnnotations = numberOfAnnotations;
        this.startDate = startDate;
    }
}
